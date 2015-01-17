var World = {
		
	markerImageResource : [],
	markerImageResourceSelected : [],
	markerList: null,
	markerImageIndicator: null,
	currentMarker: null,
	addedMessage: "",
	errorMessage: "",
	searchMessage: "",
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
		}
		catch(err){
			
			$("#status-message").html(World.errorMessage + ": " + err);	
			$("#popupInfo").popup("open");
			setTimeout(function(){
				$("#popupInfo").popup("close");
			}, 5000);	
		}
		$("#info").hide();
		
	},
	loadPoisFromJsonData: function loadPoisFromJsonDataFn(poiData, lat, lon, alt, addedMessage, errorMessage, searchMessage) {
		AR.context.destroyAll();
		World.addedMessage = addedMessage;
		World.errorMessage = errorMessage;
		World.searchMessage = searchMessage;
		PoiRadar.show();
		World.markerImageResource = [];
		World.markerImageResourceSelected = [];	
		World.markerList = [];
		World.loadBasicData();		
		var currentLocation = new AR.GeoLocation(lat, lon);
		$("#info").html(World.searchMessage);
		
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
			$("#status-message").html(poiData.length + " " + World.addedMessage);
		}
		catch(err){
			$("#status-message").html(World.errorMessage + ": " + err);
		}
		
		$("#popupInfo").popup("open");
		setTimeout(function(){
			$("#popupInfo").popup("close");
		}, 5000);	
	},
	updateDistanceToUserValues: function updateDistanceToUserValuesFn() {
		for (var i = 0; i < World.markerList.length; i++) {
			World.markerList[i].updateDistance(World.markerList[i], World.markerList[i].markerObject.locations[0].distanceToUser() / 1000.0);
		}
	},
	onPoiDetailButtonClicked: function onPoiDetailButtonClickedFn() {
		var currentMarker = World.currentMarker;
		var architectSdkUrl = "architectsdk://markerselected?id=" + encodeURIComponent(currentMarker.poiData.id);
		document.location = architectSdkUrl;
	},
}

AR.context.onLocationChanged = World.locationChanged;
AR.context.onScreenClick = World.onScreenClick;