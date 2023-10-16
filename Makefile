build_ctr:
ifndef VERSION
	echo "Please specify VERSION"
else
	./gradlew build
	
	docker build -t sfc-consolidation-simulator:$(VERSION) .
	docker save -o sfc-consolidation-simulator:$(VERSION).tar sfc-consolidation-simulator:$(VERSION)
	ctr -n k8s.io images import sfc-consolidation-simulator:$(VERSION).tar
	rm -rf sfc-consolidation-simulator:$(VERSION).tar

	docker build -t sfc-consolidation-simulator-api:$(VERSION) -f Dockerfile.api .
	docker save -o sfc-consolidation-simulator-api:$(VERSION).tar sfc-consolidation-simulator-api:$(VERSION)
	ctr -n k8s.io images import sfc-consolidation-simulator-api:$(VERSION).tar
	rm -rf sfc-consolidation-simulator-api:$(VERSION).tar
endif
