"use strict";

/**
 * 1. When user enters the explore page, they will be presented with a map with
 *    the center being their current location: 
 * 2. The map will be populated with placemarks that are closest to the center of
 *    of the map, whenver the center changes, the placemarks will change accordingly
 * 3. When user hit search, it will send a AJAX GET request to the google geolocation
 *    api, then the map will update itself with the center being the closest match
 *    to the search term. Upon center change, the bordering placemarks will change also
 */

var map = null;
var infowindow = null;
// var markers = [];

// Default to 2 Bayard Road, Pittsburgh, PA, US
var defaultPosition = {
  coords: {
    latitude: 40.454231316332056,
    longitude: -79.94300286730655
  }
};

var dummyAddresses = [

  {
    "owner": "dummy",
    "address1": "3913 Nantasket Street",
    "address2": "11",
    "postCode": "15207",
    "city": "Pittsburgh",
    "state": "Pennsylvania"
  },

  {
    "owner": "dummy1",
    "address1": "5030 Centre Avenue",
    "address2": "961",
    "postCode": "15213",
    "city": "Pittsburgh",
    "state": "Pennsylvania"
  },

  {
    "owner": "dummy2",
    "address1": "4716 Ellsworth Avenue",
    "address2": "11",
    "postCode": "15213",
    "city": "Pittsburgh",
    "state": "Pennsylvania"
  },

  {
    "owner": "dummy3",
    "address1": "2 Bayard Road",
    "address2": "23",
    "postCode": "15213",
    "city": "Pittsburgh",
    "state": "Pennsylvania"
  },

  {
    "owner": "dummy4",
    "address1": "5000 Forbes Avenue",
    "address2": "11",
    "postCode": "15213",
    "city": "Pittsburgh",
    "state": "Pennsylvania"
  },

]


var fetchClosestPosts = function (map) {

  var center = map.getCenter();
  $.ajax({
    type: "GET",
    url: "/Ucook/getClosestPosts",
    data: {
      lat: center.lat(),
      lng: center.lng(),
    },
    dataType: "json",
    success: function (res) {
      // console.log(res);
      updateMarkers(res, map, infowindow);
    },
    error: function (err) {
      alert(err);
    },
  });

};

var updateMarkers = function (data, map) {

  for (var i = 0; i < data['posts'].length; i++) {

    var post = data['posts'][i];
    var marker = new google.maps.Marker({
      id: i,
      title: post.owner + " @ " + post.address.address1,
      url: "/Ucook/detail",
      position: {
        "lat": parseFloat(post.coordinates.lat),
        "lng": parseFloat(post.coordinates.lng),
      },
      map: map,
    });
    // markers.push(marker);

    google.maps.event.addListener(marker, 'click', (function (marker, key) {
      return function () {
        var content = '<div><a target="_blank" href="' + marker.url + '">' + marker.title + '</ a></div>';
        console.log(marker.url)
        infowindow.setContent(content);
        infowindow.open(map, marker);
      };
    })(marker, i));

  }

};

var getConcatAddress = function (addr) {
  var concatAddr = addr['address1'] + ", " + addr['city'] + ", " + addr['state'];
  return concatAddr;
}

var populateMap = function (geoPosition) {

  $('#geolocation').hide();

  infowindow = new google.maps.InfoWindow({
    content: "hello"
  });

  var position = geoPosition;
  if (!position) {
    position = defaultPosition;
  }

  map = new google.maps.Map(document.getElementById('map'), {
    center: {
      lat: position.coords.latitude,
      lng: position.coords.longitude
    },
    zoom: 16
  });

  map.addListener('center_changed', function () {
    // map.addListener('dragend', function () {
    fetchClosestPosts(map);
  });

  // Make info window for marker show up above marker
  var windowOptions = {
    pixelOffset: {
      height: -32,
      width: 0
    }
  };

  fetchClosestPosts(map);

};


var geolocationFail = function () {
  populateMap(defaultPosition);
};


var initMap = function () {
  if (navigator.geolocation) {
    var location_timeout = setTimeout(geolocationFail, 5000);
    navigator.geolocation.getCurrentPosition(function (position) {
      clearTimeout(location_timeout);
      populateMap(position);
    }, function (error) {
      clearTimeout(location_timeout);
      geolocationFail();
    });
  } else {
    // Fallback for no geolocation
    geolocationFail();
  }
};


var clearMarkers = function (markers) {
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
  }
  markers = [];
};

var calDist = function (lat1, lon1, lat2, lon2, unit) {
  if ((lat1 == lat2) && (lon1 == lon2)) {
    return 0;
  } else {
    var radlat1 = Math.PI * lat1 / 180;
    var radlat2 = Math.PI * lat2 / 180;
    var theta = lon1 - lon2;
    var radtheta = Math.PI * theta / 180;
    var dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
    if (dist > 1) {
      dist = 1;
    }
    dist = Math.acos(dist);
    dist = dist * 180 / Math.PI;
    dist = dist * 60 * 1.1515;
    if (unit == "K") {
      dist = dist * 1.609344
    }
    if (unit == "N") {
      dist = dist * 0.8684
    }
    return dist;
  }
}

$('#id_search_form').on('submit', function (event) {

  event.preventDefault();
  // clearMarkers(markers);

  var searchTerm = $('#id_term').val();
  var center = map.center;
  var geocoder = new google.maps.Geocoder();

  geocoder.geocode({
    'address': searchTerm,
  }, function (res, status) {
    if (status === 'OK') {

      var minDist = Number.MAX_VALUE;;
      var closestIdx = 0;
      for (var i = 0; i < res.length; i++) {
        var loc = res[i].geometry.location;
        var dist = calDist(loc.lat(), loc.lng(), center.lat(), center.lng())
        if (dist < minDist) {
          minDist = dist;
          closestIdx = i;
        }
      }

      map.setCenter(res[closestIdx].geometry.location);

      var marker = new google.maps.Marker({
        id: searchTerm,
        title: searchTerm,
        url: "#",
        position: res[closestIdx].geometry.location,
        map: map,
        icon: {
          url: "http://maps.google.com/mapfiles/ms/icons/yellow-dot.png",
        },
      });

      google.maps.event.addListener(marker, 'click', function () {
        return function () {
          var content = '<div><a target="_blank" href="' + marker.url + '">' + marker.title + '</ a></div>';
          infowindow.setContent(content);
          infowindow.open(map, marker);
        };
      });

    } else {
      alert('Geocode was not successful for the following reason: ' + status);
    }
  });

});