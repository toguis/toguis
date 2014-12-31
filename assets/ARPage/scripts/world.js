var World = {
		
	markerImageResource : [],
	markerImageResourceSelected : [],
	markerList: null,
	markerRawData: null,
	markerImageIndicator: null,
	currentMarker: null,
	resourceLoaded: false,
	loadBasicData : function loadBasicDataFn(){
		World.markerImageResource.push(new AR.ImageResource("images/ar_monument.png"));
		World.markerImageResource.push(new AR.ImageResource("images/ar_museum.png"));
		World.markerImageResource.push(new AR.ImageResource("images/ar_hotel.png"));
		World.markerImageResource.push(new AR.ImageResource("images/ar_restaurant.png"));
		World.markerImageResource.push(new AR.ImageResource("images/ar_interest.png"));
		World.markerImageResource.push(new AR.ImageResource("images/ar_building.png"));
		World.markerImageResource.push(new AR.ImageResource("images/ar_transport.png"));
		World.markerImageResource.push(new AR.ImageResource("images/ar_event.png"));
			
		World.markerImageResourceSelected.push(new AR.ImageResource("images/ar_monument_sel.png"));
		World.markerImageResourceSelected.push(new AR.ImageResource("images/ar_museum_sel.png"));
		World.markerImageResourceSelected.push(new AR.ImageResource("images/ar_hotel_sel.png"));
		World.markerImageResourceSelected.push(new AR.ImageResource("images/ar_restaurant_sel.png"));
		World.markerImageResourceSelected.push(new AR.ImageResource("images/ar_interest_sel.png"));
		World.markerImageResourceSelected.push(new AR.ImageResource("images/ar_building_sel.png"));
		World.markerImageResourceSelected.push(new AR.ImageResource("images/ar_transport_sel.png"));
		World.markerImageResourceSelected.push(new AR.ImageResource("images/ar_event_sel.png"));
		
		World.markerImageIndicator = new AR.ImageResource("images/indicator.png");
	},
	onMarkerSelected: function onMarkerSelectedFn(marker) {

		// deselect previous marker
		if (World.currentMarker) {
			if (World.currentMarker.poiData.id == marker.poiData.id) {
				return;
			}
			World.currentMarker.setDeselected(World.currentMarker);
		}

		// highlight current one
		marker.setSelected(marker);
		World.currentMarker = marker;
	},
	onScreenClick: function onScreenClickFn() {
		if (World.currentMarker) {
			World.currentMarker.setDeselected(World.currentMarker);
		}
	},
	locationChanged: function locationChangedFn(lat, lon, alt, acc) {
		var info = document.getElementById("info");
		try{
			World.updateDistanceToUserValues();
			//info.innerHTML = "Todo OK " + World.markerList[0].poiData.latitude;
		}
		catch(err){
			
			info.innerHTML = "Error trying retrieving data: " + err;				
		}
		
		//World.loadBasicData();
		//World.loadPoisFromJsonData(World.markerRawData,lat,lon,alt);
		//World.loadBasicData();
		/*var poiData = {
				"id": 1,
				"longitude": (lon + 0.0002),
				"latitude": (lat + 0.0002),
				"altitude": 100.0,
				"distance" : 0,
				"type" : 1,
				"title": "el gato del rio",
				"description": "el gato del rio"
			};	
		var currentLocation = new AR.GeoLocation(lat, lon);
		var markerLocation = new AR.GeoLocation(poiData.latitude, poiData.longitude, poiData.altitude);
		var distance = currentLocation.distanceTo(markerLocation) / 1000.0;
		poiData.distance = distance;
		//World.markerList.push(new POIMarker(poiData));
		var poiData2 = {
				"id": 2,
				"longitude": (lon - 0.01),
				"latitude": (lat - 0.01),
				"altitude": 100.0,
				"distance" : 0,
				"type" : 1,
				"title": "hotel inter",
				"description":"hotel inter"
			};	
		markerLocation = new AR.GeoLocation(poiData2.latitude, poiData2.longitude, poiData2.altitude);
		distance = currentLocation.distanceTo(markerLocation) / 1000.0;
		poiData2.distance = distance;
		var data = '['+ JSON.stringify(poiData) + ',' + JSON.stringify(poiData2) + ']';
	    World.loadPoisFromJsonData(JSON.parse(data), lat,lon,alt);
		
		/*World.markerList.push(new POIMarker(poiData2));
		
		/*var poiImage = new AR.ImageResource("images/ar_monument.png");
		var markerLocation = new AR.GeoLocation(poiData.latitude, poiData.longitude, poiData.altitude);
		var markerImageDrawable_idle = new AR.ImageDrawable(poiImage, 2.5, {
			zOrder: 0,
			opacity: 1.0
		});

		var location1 = new AR.GeoLocation(lat, lon);
		var location2 = new AR.GeoLocation(poiData.latitude, poiData.longitude);
		var dist = location1.distanceTo(location2) / 1000.0;
		
		var titleLabel = new AR.Label(poiData.title, 0.3, {
	        zOrder: 1,
	        offsetY: -1.1,
	        style: {
	            textColor: '#ffffff',
	            fontStyle: AR.CONST.FONT_STYLE.BOLD
	        }
	    });
		
		var distLabel = new AR.Label(dist.toFixed(2).toString() + ' Km', 0.25, {
	        zOrder: 1,
	        offsetY: -0.75,
	        style: {
	            textColor: '#ffffff',
	            fontStyle: AR.CONST.FONT_STYLE.BOLD
	        }
	    });
		
		var markerObject = new AR.GeoObject(markerLocation, {
			drawables: {
				cam: [markerImageDrawable_idle, titleLabel, distLabel]
			}
		});
		
		var info = document.getElementById("info");
		info.innerHTML = "2 elements added";*/
		
		
	},
	loadPoisFromJsonData: function loadPoisFromJsonDataFn(poiData, lat, lon, alt) {
		AR.context.destroyAll();
		World.markerImageResource = [];
		World.markerImageResourceSelected = [];	
		World.markerList = [];
		World.loadBasicData();		
		World.markerRawData = "hola";
		var currentLocation = new AR.GeoLocation(lat, lon);
		var info = document.getElementById("info");
		info.innerHTML = poiData.length + " elements counted";	
		
		try{
			for(i = 0; i < poiData.length ; i++){
				var markerLocation = new AR.GeoLocation(parseFloat(poiData[i].latitude), parseFloat(poiData[i].longitude));
				var distance = currentLocation.distanceTo(markerLocation) / 1000.0;
				var singlePoi = {
					"id": parseInt(poiData[i].id),
					"latitude": parseFloat(poiData[i].latitude),
					"longitude": parseFloat(poiData[i].longitude),
					"altitude": parseFloat(poiData[i].altitude),
					"title": poiData[i].title,
					"description": poiData[i].description,
					"distance":distance,
					"type": parseInt(poiData[i].type)									
				};	
				World.markerList.push(new POIMarker(singlePoi));
			}	
			info.innerHTML = poiData.length + " elements added ";
			
		}
		catch(err){
			info.innerHTML = "Error trying retrieving data: " + err;
		}
	},
	updateDistanceToUserValues: function updateDistanceToUserValuesFn() {
		for (var i = 0; i < World.markerList.length; i++) {
			World.markerList[i].updateDistance(World.markerList[i], World.markerList[i].markerObject.locations[0].distanceToUser() / 1000.0);
		}
	}
}

AR.context.onLocationChanged = World.locationChanged;
AR.context.onScreenClick = World.onScreenClick;