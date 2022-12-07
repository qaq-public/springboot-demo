TAG?=1.0.1
NAME:=demo-server
PORT?=9999
DOCKER_REPOSITORY:=blacklee123
DOCKER_IMAGE_NAME:=$(DOCKER_REPOSITORY)/$(NAME)
GIT_COMMIT:=$(shell git describe --dirty --always)
VERSION:=$(shell grep 'image: $(DOCKER_IMAGE_NAME):' kustomize/deployment.yaml | awk -F: '{ print $$3}')
EXTRA_RUN_ARGS?=

.PHONY: test-version
test-version:
	@echo "$(VERSION)"
#测试获取的version是否正确

build:
	mvn clean package
	docker build --platform linux/amd64 -f Dockerfile -t $(DOCKER_IMAGE_NAME):$(VERSION) .

test: build
	@docker rm -f $(NAME) || true
	@docker run -dp $(PORT):$(PORT) --name=$(NAME) $(DOCKER_IMAGE_NAME):$(VERSION)
	@docker ps | grep $(DOCKER_IMAGE_NAME)
	#@curl http://localhost:10086/swagger-ui.html

push:
	docker tag $(DOCKER_IMAGE_NAME):$(VERSION) $(DOCKER_IMAGE_NAME):latest
	docker push $(DOCKER_IMAGE_NAME):$(VERSION)
	docker push $(DOCKER_IMAGE_NAME):latest

version-set-mac:
	next="$(TAG)" && \
	current="$(VERSION)" && \
	sed -i '' "s/$(NAME):$$current/$(NAME):$$next/g" kustomize/deployment.yaml && \
	sed -i '' "s/$(NAME)-$$current/$(NAME)-$$next/g" Dockerfile && \
	sed -i '' "s/<version>$$current<\/version>/<version>$$next<\/version>/g" pom.xml && \
	echo "Version $$next set in pom, deployment , Dockerfile"

version-set:
	next="$(TAG)" && \
	current="$(VERSION)" && \
	sed -i "s/$(NAME):$$current/$(NAME):$$next/g" kustomize/deployment.yaml && \
	sed -i "s/$(NAME)-$$current/$(NAME)-$$next/g" Dockerfile && \
	sed -i "s/<version>$$current<\/version>/<version>$$next<\/version>/g" pom.xml && \
	echo "Version $$next set in pom, deployment , Dockerfile"

release:
	git tag $(VERSION)
	git push origin $(VERSION)

update:
	kubectl -n qa-dev get pods | grep demo-server | cut -d " " -f 1 | xargs kubectl -n qa-dev delete pod