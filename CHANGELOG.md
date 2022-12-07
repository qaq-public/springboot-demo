`antd` 严格遵循 [Semantic Versioning 2.0.0](http://semver.org/lang/zh-CN/) 语义化版本规范。

#### 发布周期

- 修订版本号：每周末会进行日常 bugfix 更新。（如果有紧急的 bugfix，则任何时候都可发布）
- 次版本号：每月发布一个带有新特性的向下兼容的版本。
- 主版本号：含有破坏性更新和新特性，不在发布周期内。

---

## 1.0.1

`2022-05-30`

- 🐞 修复了一个tapd的问题。[#1142833848002712997](https://www.tapd.bilibili.co/42833848/prong/stories/view/1142833848002712997) [@李白](https://git.bilibili.co/lifajin)


## 1.0.0

`2022-05-22`

- 🐞 修复 DatePicker 初次打开时 placeholder 闪烁的问题。[#35620](https://github.com/ant-design/ant-design/pull/35620) [@yykoypj](https://github.com/yykoypj)
- 🛠 移除 Grid 默认 `role` 标签，以使其更好的适配 [aria-required-parent](https://accessibilityinsights.io/info-examples/web/aria-required-parent/) 要求。[#35616](https://github.com/ant-design/ant-design/pull/35616) [@bartpio](https://github.com/bartpio)
- 🐞 修复 Anchor 在某些游览器下会被切割内容的问题。[#35612](https://github.com/ant-design/ant-design/pull/35612) [@josonho](https://github.com/josonho)
- 🐞 修复 Table 存在表头分组和垂直滚动条时表头边框异常的问题。[#35591](https://github.com/ant-design/ant-design/pull/35591)
- 🐞 修复 Drawer 内按钮关闭速度过快问题。[#35339](https://github.com/ant-design/ant-design/pull/35339)
- 🐞 修复 Segmented 组件中选项使用 icon 属性时图标与文字之间的间距失效问题。[#35701](https://github.com/ant-design/ant-design/pull/35701)
- 💄 优化 Popover 的箭头效果。[#35717](https://github.com/ant-design/ant-design/pull/35717)
- TypeScript
    - 🤖 修复 Card 组件的类型提示。[#35753](https://github.com/ant-design/ant-design/pull/35753)
