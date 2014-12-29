String.prototype.trunc = function(n) {
    return this.substr(0, n - 1) + (this.length > n ? '...' : '');
};
function POIMarker(poiData){
	this.poiData = poiData;
	this.isSelected = false;
	var markerLocation = new AR.GeoLocation(poiData.latitude, poiData.longitude, poiData.altitude);
    this.animationGroup_idle = null;
    this.animationGroup_selected = null;
    
	this.drawable_idle = new AR.ImageDrawable(World.markerImageResource[poiData.type - 1], 2.5, {
        zOrder: 0,
        opacity: 1.0,
        onClick: POIMarker.prototype.getOnClickTrigger(this)
    });
	
	this.drawable_sel = new AR.ImageDrawable(World.markerImageResourceSelected[poiData.type - 1], 2.5, {
        zOrder: 0,
        opacity: 0.0,
    });
	
	this.titleLabel = new AR.Label(poiData.title.trunc(20), 0.3, {
        zOrder: 1,
        offsetY: -1.1,
        style: {
            textColor: '#ffffff',
            fontStyle: AR.CONST.FONT_STYLE.BOLD
        }
    });
	
	this.distLabel = new AR.Label(poiData.distance.toFixed(2).toString() + ' Km', 0.25, {
        zOrder: 1,
        offsetY: -0.75,
        style: {
            textColor: '#ffffff',
            fontStyle: AR.CONST.FONT_STYLE.BOLD
        }
    });
	
    this.directionIndicatorDrawable = new AR.ImageDrawable(World.markerImageIndicator, 0.1, {
        enabled: false,
        verticalAnchor: AR.CONST.VERTICAL_ANCHOR.TOP
    });
    
    this.markerObject = new AR.GeoObject(markerLocation, {
        drawables: {
            cam: [this.drawable_idle, this.drawable_sel, this.titleLabel, this.distLabel],
            indicator: this.directionIndicatorDrawable
        }
    });
    

}
POIMarker.prototype.getOnClickTrigger = function(marker) {
    return function() {
        if (marker.isSelected) {
        	POIMarker.prototype.setDeselected(marker);
        } else {
        	POIMarker.prototype.setSelected(marker);
            try {
                World.onMarkerSelected(marker);
            } catch (err) {
                alert(err);
            }
        }
        return true;
    };
};

POIMarker.prototype.setSelected = function(marker) {

    marker.isSelected = true;

    if (marker.animationGroup_selected === null) {

        var hideIdleDrawableAnimation = new AR.PropertyAnimation(marker.drawable_idle, "opacity", null, 0.0, 500);
        var showSelectedDrawableAnimation = new AR.PropertyAnimation(marker.drawable_sel, "opacity", null, 1.0, 500);

        var idleDrawableResizeAnimation = new AR.PropertyAnimation(marker.drawable_idle, 'scaling', null, 1.2, 1000, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));

        var selectedDrawableResizeAnimation = new AR.PropertyAnimation(marker.drawable_sel, 'scaling', null, 1.2, 1000, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));
 
        var titleLabelResizeAnimation = new AR.PropertyAnimation(marker.titleLabel, 'scaling', null, 1.3, 1000, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));
        
        var titleLabelMoveAnimation = new AR.PropertyAnimation(marker.titleLabel, 'offsetY', null, -1.3, 1000, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));
        
        var distanceLabelResizeAnimation = new AR.PropertyAnimation(marker.distLabel, 'scaling', null, 1.2, 1000, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));

        var distanceLabelMoveAnimation = new AR.PropertyAnimation(marker.distLabel, 'offsetY', null, -0.90, 1000, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));
        
        marker.animationGroup_selected = new AR.AnimationGroup(AR.CONST.ANIMATION_GROUP_TYPE.PARALLEL, [hideIdleDrawableAnimation, showSelectedDrawableAnimation, idleDrawableResizeAnimation, selectedDrawableResizeAnimation, titleLabelResizeAnimation, titleLabelMoveAnimation, distanceLabelResizeAnimation, distanceLabelMoveAnimation]);
    }    

    marker.directionIndicatorDrawable.enabled = true;
    marker.drawable_idle.onClick = null;
    marker.drawable_sel.onClick = POIMarker.prototype.getOnClickTrigger(marker);
    marker.animationGroup_selected.start();
};

POIMarker.prototype.setDeselected = function(marker) {

    marker.isSelected = false;
    
    if (marker.animationGroup_idle === null) {

        var showIdleDrawableAnimation = new AR.PropertyAnimation(marker.drawable_idle, "opacity", null, 1.0, 500);
        var hideSelectedDrawableAnimation = new AR.PropertyAnimation(marker.drawable_sel, "opacity", null, 0.0, 500);

        var idleDrawableResizeAnimation = new AR.PropertyAnimation(marker.drawable_idle, 'scaling', null, 1.0, 1000, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));

        var selectedDrawableResizeAnimation = new AR.PropertyAnimation(marker.drawable_sel, 'scaling', null, 1.0, 1000, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));

        var titleLabelResizeAnimation = new AR.PropertyAnimation(marker.titleLabel, 'scaling', null, 1.0, 1000, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));
        
        var titleLabelMoveAnimation = new AR.PropertyAnimation(marker.titleLabel, 'offsetY', null, -1.1, 1000, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));
        
        var distanceLabelResizeAnimation = new AR.PropertyAnimation(marker.distLabel, 'scaling', null, 1.0, 1000, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));

        var distanceLabelMoveAnimation = new AR.PropertyAnimation(marker.distLabel, 'offsetY', null, -0.75, 1000, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));
        marker.animationGroup_idle = new AR.AnimationGroup(AR.CONST.ANIMATION_GROUP_TYPE.PARALLEL, [showIdleDrawableAnimation, hideSelectedDrawableAnimation, idleDrawableResizeAnimation, selectedDrawableResizeAnimation, titleLabelResizeAnimation,titleLabelMoveAnimation, distanceLabelResizeAnimation, distanceLabelMoveAnimation]);
    }

    marker.directionIndicatorDrawable.enabled = false;
    marker.drawable_idle.onClick = POIMarker.prototype.getOnClickTrigger(marker);
    marker.drawable_sel.onClick = null;
    marker.animationGroup_idle.start();
};

