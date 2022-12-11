PACKAGES := "crap"

.PHONY: build
build:
	docker build . -f Dockerfile --tag sajor:crap

.PHONY: run
run:
	docker run --name crap-server -d -p2727:2727 sajor:crap
