package com.qaq.demo.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.qaq.demo.utils.HttpContextUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/***
 * HttpServletRequest 过滤器
 * 解决: request.getInputStream()只能读取一次的问题
 * 目标: 流可重复读
 */
@Component
@WebFilter(filterName = "HttpServletRequestFilter", urlPatterns = "/")
@Order(10000)
public class HttpServletRequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            String contentType = servletRequest.getContentType();
            // 如果处理上传文件数据，下面方法执行到chain.doFilter()时会出线异常，所以此处只处理@RequestBody数据
            if (!ObjectUtils.isEmpty(contentType) && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
            }
        }
        // 获取请求中的流如何，将取出来的字符串，再次转换成流，然后把它放入到新request对象中
        // 在chain.doFiler方法中传递新的request对象
        if (null == requestWrapper) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }

    @Override
    public void destroy() {
    }

    /***
     * HttpServletRequest 包装器
     * 解决: request.getInputStream()只能读取一次的问题
     * 目标: 流可重复读
     */
    public class RequestWrapper extends HttpServletRequestWrapper {
        /**
         * 请求体
         */
        private String mBody;

        public RequestWrapper(HttpServletRequest request) {
            super(request);
            // 将body数据存储起来
            mBody = getBody(request);
        }

        /**
         * 获取请求体
         * 
         * @param request 请求
         * @return 请求体
         */
        private String getBody(HttpServletRequest request) {
            return HttpContextUtils.getBodyString(request);
        }

        /**
         * 获取请求体
         * 
         * @return 请求体
         */
        public String getBody() {
            return mBody;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            // 创建字节数组输入流
            final ByteArrayInputStream bais = new ByteArrayInputStream(mBody.getBytes(StandardCharsets.UTF_8));
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }

                @Override
                public int read() throws IOException {
                    return bais.read();
                }
            };
        }
    }
}
