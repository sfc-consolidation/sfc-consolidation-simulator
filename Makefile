build_ctr:
ifndef VERSION
	echo "Please specify VERSION"
else
	./gradlew build
	docker build -t sfc-consolidation-simulator:$(VERSION) .
	sudo docker save -o sfc-consolidation-simulator:$(VERSION).tar sfc-consolidation-simulator:$(VERSION)
	sudo ctr -n k8s.io images import sfc-consolidation-simulator:$(VERSION).tar
	rm -rf sfc-consolidation-simulator:$(VERSION).tar
endif
