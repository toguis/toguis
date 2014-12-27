var World = {
	locationChanged: function locationChangedFn(lat, lon, alt, acc) {
		var poiData = {
				"id": 1,
				"longitude": (lon + (Math.random() / 5 - 0.1)),
				"latitude": (lat + (Math.random() / 5 - 0.1)),
				"altitude": 100.0
			};	
		var poiImage = new AR.ImageResource("images/marker_idle.png");
		var markerLocation = new AR.GeoLocation(poiData.latitude, poiData.longitude, poiData.altitude);
		var markerImageDrawable_idle = new AR.ImageDrawable(poiImage, 2.5, {
			zOrder: 0,
			opacity: 1.0
		});
		
		var markerObject = new AR.GeoObject(markerLocation, {
			drawables: {
				cam: [markerImageDrawable_idle]
			}
		});
		
		var info = document.getElementById("info");
		info.innerHTML = "1 element added";
		
	}
}

AR.context.onLocationChanged = World.locationChanged;