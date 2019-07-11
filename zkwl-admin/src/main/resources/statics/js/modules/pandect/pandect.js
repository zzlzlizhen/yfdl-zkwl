$(function () {
    m_p()
})
//地图方法
function m_p() {
    // 百度地图API功能

    var map = new BMap.Map("allmap");    // 创建Map实例
    var geolocation = new BMap.Geolocation();
    geolocation.getCurrentPosition(function(r){
        map.centerAndZoom(new BMap.Point(118.777882,32.059839),9);
        //map.centerAndZoom(new BMap.Point(118.777882,32.059839), 12);  // 初始化地图,设置中心点坐标和地图级别
        map.addControl(new BMap.OverviewMapControl());
        map.setCurrentCity("北京市");          // 设置地图显示的城市 此项是必须设置的
        map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放

        var arr=[{"x":118.777882,"y":32.059839},{"x":118.777882,"y":32.059839}]
        var xy //地图数组
        var color="#cccccc"  //背景颜色
        var urla //背景圈
        if("0" == "0"){
            //全部
            xy=arr;
            color='#4783E7';
        }

    var markers = [];
    var pt = null;
    for (var i in xy) {
        pt = new BMap.Point(xy[i].x , xy[i].y);
        markers.push(new BMap.Marker(pt));
    }
    //最简单的用法，生成一个marker数组，然后调用markerClusterer类即可。

    // var markerClusterer = new BMapLib.MarkerClusterer(map,{
    //     markers:markers,
    //     girdSize : 100,
    //     styles : [{
    //         url:urla,
    //         size: new BMap.Size(92, 92),
    //         backgroundColor :color,
    //     }
    //     ],
    // });
    markerClusterer.setMaxZoom(13);
    markerClusterer.setGridSize(100);


    var styleJson =[
        {
        "featureType": "land",
        "elementType": "geometry",
        "stylers": {
            "color": "#3487dcff",
            "visibility": "on"
        }
    }, {
        "featureType": "water",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#ffffffff",
            "visibility": "on"
        }
    }, {
        "featureType": "building",
        "elementType": "geometry.fill",
        "stylers": {
            "color": "#00b2a9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "building",
        "elementType": "geometry.stroke",
        "stylers": {
            "color": "#00a090ff",
            "visibility": "on"
        }
    }, {
        "featureType": "water",
        "elementType": "geometry",
        "stylers": {
            "color": "#ffffffff",
            "visibility": "on"
        }
    }, {
        "featureType": "village",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "town",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "district",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "country",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#ffffffff",
            "visibility": "on"
        }
    }, {
        "featureType": "city",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "continent",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "poilabel",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "poilabel",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "scenicspotslabel",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "scenicspotslabel",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "transportationlabel",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "transportationlabel",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "airportlabel",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "airportlabel",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "road",
        "elementType": "geometry.fill",
        "stylers": {
            "color": "#68d8d6ff",
            "visibility": "on"
        }
    }, {
        "featureType": "road",
        "elementType": "geometry.stroke",
        "stylers": {
            "color": "#4ab5aeff",
            "visibility": "on"
        }
    }, {
        "featureType": "road",
        "elementType": "geometry",
        "stylers": {
            "weight": 3
        }
    }, {
        "featureType": "green",
        "elementType": "geometry",
        "stylers": {
            "color": "#3dccc7ff",
            "visibility": "on"
        }
    }, {
        "featureType": "scenicspots",
        "elementType": "geometry",
        "stylers": {
            "color": "#07beb8ff",
            "visibility": "on"
        }
    }, {
        "featureType": "scenicspots",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "scenicspots",
        "elementType": "labels.text.stroke",
        "stylers": {
            "weight": 1,
            "color": "#9ceaefff",
            "visibility": "on"
        }
    }, {
        "featureType": "continent",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "country",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "city",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "city",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "scenicspotslabel",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "airportlabel",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "transportationlabel",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "railway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "subway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "highwaysign",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "nationalwaysign",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "nationalwaysign",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "provincialwaysign",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "provincialwaysign",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "tertiarywaysign",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "tertiarywaysign",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "subwaylabel",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "subwaylabel",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "road",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on",
            "weight": 90
        }
    }, {
        "featureType": "road",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "shopping",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "scenicspots",
        "elementType": "labels",
        "stylers": {
            "visibility": "on"
        }
    }, {
        "featureType": "scenicspotslabel",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "manmade",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "manmade",
        "elementType": "labels",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "highwaysign",
        "elementType": "labels.icon",
        "stylers": {
            "visibility": "off"
        }
    }, {
        "featureType": "water",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#3dccc700",
            "visibility": "on"
        }
    }, {
        "featureType": "road",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "6"
        }
    }, {
        "featureType": "road",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "7"
        }
    }, {
        "featureType": "road",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "8"
        }
    }, {
        "featureType": "road",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "9"
        }
    }, {
        "featureType": "road",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "6"
        }
    }, {
        "featureType": "road",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "7"
        }
    }, {
        "featureType": "road",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "8"
        }
    }, {
        "featureType": "road",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "9"
        }
    }, {
        "featureType": "road",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "6"
        }
    }, {
        "featureType": "road",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "7"
        }
    }, {
        "featureType": "road",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "8"
        }
    }, {
        "featureType": "road",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "9"
        }
    }, {
        "featureType": "road",
        "elementType": "labels.text",
        "stylers": {
            "fontsize": 24
        }
    }, {
        "featureType": "highway",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "highway",
        "elementType": "geometry.fill",
        "stylers": {
            "color": "#68d8d6ff",
            "visibility": "on"
        }
    }, {
        "featureType": "highway",
        "elementType": "geometry.stroke",
        "stylers": {
            "color": "#1c4f7eff"
        }
    }, {
        "featureType": "highway",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "highway",
        "elementType": "geometry",
        "stylers": {
            "weight": 3
        }
    }, {
        "featureType": "nationalway",
        "elementType": "geometry.fill",
        "stylers": {
            "color": "#68d8d6ff",
            "visibility": "on"
        }
    }, {
        "featureType": "nationalway",
        "elementType": "geometry.stroke",
        "stylers": {
            "color": "#1c4f7eff"
        }
    }, {
        "featureType": "nationalway",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "nationalway",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "nationalway",
        "elementType": "geometry",
        "stylers": {
            "weight": 3
        }
    }, {
        "featureType": "provincialway",
        "elementType": "geometry.fill",
        "stylers": {
            "color": "#68d8d6ff",
            "visibility": "on"
        }
    }, {
        "featureType": "cityhighway",
        "elementType": "geometry.fill",
        "stylers": {
            "color": "#68d8d6ff",
            "visibility": "on"
        }
    }, {
        "featureType": "arterial",
        "elementType": "geometry.fill",
        "stylers": {
            "color": "#68d8d6ff",
            "visibility": "on"
        }
    }, {
        "featureType": "tertiaryway",
        "elementType": "geometry.fill",
        "stylers": {
            "color": "#68d8d6ff",
            "visibility": "on"
        }
    }, {
        "featureType": "fourlevelway",
        "elementType": "geometry.fill",
        "stylers": {
            "color": "#68d8d6ff",
            "visibility": "on"
        }
    }, {
        "featureType": "local",
        "elementType": "geometry.fill",
        "stylers": {
            "color": "#68d8d6ff",
            "visibility": "on"
        }
    }, {
        "featureType": "provincialway",
        "elementType": "geometry.stroke",
        "stylers": {
            "color": "#4ab5aeff",
            "visibility": "on"
        }
    }, {
        "featureType": "cityhighway",
        "elementType": "geometry.stroke",
        "stylers": {
            "color": "#4ab5aeff",
            "visibility": "on"
        }
    }, {
        "featureType": "arterial",
        "elementType": "geometry.stroke",
        "stylers": {
            "color": "#4ab5aeff",
            "visibility": "on"
        }
    }, {
        "featureType": "tertiaryway",
        "elementType": "geometry.stroke",
        "stylers": {
            "color": "#4ab5aeff",
            "visibility": "on"
        }
    }, {
        "featureType": "fourlevelway",
        "elementType": "geometry.stroke",
        "stylers": {
            "color": "#4ab5aeff",
            "visibility": "on"
        }
    }, {
        "featureType": "local",
        "elementType": "geometry.stroke",
        "stylers": {
            "color": "#4ab5aeff",
            "visibility": "on"
        }
    }, {
        "featureType": "local",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "local",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "fourlevelway",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "tertiaryway",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "arterial",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "cityhighway",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "provincialway",
        "elementType": "labels.text.fill",
        "stylers": {
            "color": "#c4fff9ff",
            "visibility": "on"
        }
    }, {
        "featureType": "provincialway",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "cityhighway",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "arterial",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "tertiaryway",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "fourlevelway",
        "elementType": "labels.text.stroke",
        "stylers": {
            "color": "#9ceaefff",
            "visibility": "on",
            "weight": 1
        }
    }, {
        "featureType": "fourlevelway",
        "elementType": "geometry",
        "stylers": {
            "weight": 1
        }
    }, {
        "featureType": "tertiaryway",
        "elementType": "geometry",
        "stylers": {
            "weight": 1
        }
    }, {
        "featureType": "local",
        "elementType": "geometry",
        "stylers": {
            "weight": 1
        }
    }, {
        "featureType": "provincialway",
        "elementType": "geometry",
        "stylers": {
            "weight": 3
        }
    }, {
        "featureType": "cityhighway",
        "elementType": "geometry",
        "stylers": {
            "weight": 3
        }
    }, {
        "featureType": "arterial",
        "elementType": "geometry",
        "stylers": {
            "weight": 3
        }
    }, {
        "featureType": "highway",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "6"
        }
    }, {
        "featureType": "highway",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "7"
        }
    }, {
        "featureType": "highway",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "8"
        }
    }, {
        "featureType": "highway",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "9"
        }
    }, {
        "featureType": "highway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "6"
        }
    }, {
        "featureType": "highway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "7"
        }
    }, {
        "featureType": "highway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "8"
        }
    }, {
        "featureType": "highway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "9"
        }
    }, {
        "featureType": "highway",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "6"
        }
    }, {
        "featureType": "highway",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "7"
        }
    }, {
        "featureType": "highway",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "8"
        }
    }, {
        "featureType": "highway",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "9"
        }
    }, {
        "featureType": "nationalway",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "6"
        }
    }, {
        "featureType": "nationalway",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "7"
        }
    }, {
        "featureType": "nationalway",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "8"
        }
    }, {
        "featureType": "nationalway",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "9"
        }
    }, {
        "featureType": "nationalway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "6"
        }
    }, {
        "featureType": "nationalway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "7"
        }
    }, {
        "featureType": "nationalway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "8"
        }
    }, {
        "featureType": "nationalway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "9"
        }
    }, {
        "featureType": "nationalway",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "6"
        }
    }, {
        "featureType": "nationalway",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "7"
        }
    }, {
        "featureType": "nationalway",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "8"
        }
    }, {
        "featureType": "nationalway",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "9"
        }
    }, {
        "featureType": "provincialway",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "8,10",
            "level": "8"
        }
    }, {
        "featureType": "provincialway",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "8,10",
            "level": "9"
        }
    }, {
        "featureType": "provincialway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "8,10",
            "level": "8"
        }
    }, {
        "featureType": "provincialway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "8,10",
            "level": "9"
        }
    }, {
        "featureType": "provincialway",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "8,10",
            "level": "8"
        }
    }, {
        "featureType": "provincialway",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "8,10",
            "level": "9"
        }
    }, {
        "featureType": "cityhighway",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "6"
        }
    }, {
        "featureType": "cityhighway",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "7"
        }
    }, {
        "featureType": "cityhighway",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "8"
        }
    }, {
        "featureType": "cityhighway",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "9"
        }
    }, {
        "featureType": "cityhighway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "6"
        }
    }, {
        "featureType": "cityhighway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "7"
        }
    }, {
        "featureType": "cityhighway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "8"
        }
    }, {
        "featureType": "cityhighway",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "9"
        }
    }, {
        "featureType": "cityhighway",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "6"
        }
    }, {
        "featureType": "cityhighway",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "7"
        }
    }, {
        "featureType": "cityhighway",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "8"
        }
    }, {
        "featureType": "cityhighway",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "6,9",
            "level": "9"
        }
    }, {
        "featureType": "arterial",
        "stylers": {
            "curZoomRegionId": "0",
            "curZoomRegion": "9,9",
            "level": "9"
        }
    }, {
        "featureType": "arterial",
        "elementType": "geometry",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "9,9",
            "level": "9"
        }
    }, {
        "featureType": "arterial",
        "elementType": "labels",
        "stylers": {
            "visibility": "off",
            "curZoomRegionId": "0",
            "curZoomRegion": "9,9",
            "level": "9"
        }
    }
    ]
    map.setMapStyleV2({styleJson:styleJson});

    });
}