import json
import requests
import numpy as np
import operator

import googlemaps
import geopy.distance


def recommendByFoodType(posts, preferred, exclude=None):
    recs = []
    for post in posts:
        if post["foodType"] == preferred:
            if exclude is None:
                recs.append(post)
            else:
                if post not in exclude:
                    recs.append(post)
    return recs


def recommendAtRandom(posts, num_recs=5, exclude=None):
    recs = []
    rand_indices = np.random.randint(low=0, high=len(posts), size=num_recs)
    for i in range(len(posts)):
        if i in rand_indices:
            post = posts[i]
            if exclude is None:
                recs.append(post)
            else:
                if post not in exclude:
                    recs.append(post)
    return recs


def dist(lat1, lng1, lat2, lng2):

    if not isinstance(lat1, float):
        lat1 = float(lat1)
    if not isinstance(lng1, float):
        lng1 = float(lng1)
    if not isinstance(lat2, float):
        lat2 = float(lat2)
    if not isinstance(lng2, float):
        lng2 = float(lng2)

    dist = geopy.distance.vincenty((lat1, lng1), (lat2, lng2)).km

    return dist


def _rankByDist(recs, center_lat, center_lng):
    sorted(
        recs,
        key=lambda rec: dist(
            rec["coordinates"]["lat"], rec["coordinates"]["lng"], center_lat, center_lng
        ),
    )
    return recs


def _rankByDate(recs):
    sorted(recs, key=lambda rec: rec["preferDate"])
    return recs


def rankRecs(recs, key="loc", *args):
    if key == "loc" or key == "location":
        return _rankByDist(recs, *args)
    return _rankByDate(recs)


def hasKey(dic, key):
    if key in dic:
        return True
    return False


def getDummyPosts():
    url = "https://mysterious-retreat-42657.herokuapp.com/WebApplication?parameter=getAllHostPost"
    response = requests.get(url)
    content = response.text

    if content.__str__().__len__() == 0:
        return

    posts = eval(content.__str__())
    posts = list(
        filter(
            lambda obj: hasKey(obj, "coordinates") and hasKey(
                obj, "preferDate"), posts
        )
    )
    _posts = [
        {
            "postID": 1,
            "owner": "dummy",
            "address1": "3913 Nantasket Street",
            "address2": "11",
            "postCode": "15207",
            "city": "Pittsburgh",
            "state": "Pennsylvania",
            "foodType": "Chinese",
            "date": "20190410",
            "coordinates": {"lat": 40.4273999, "lng": -79.9403981},
        },
        {
            "postID": 2,
            "owner": "dummy1",
            "address1": "5030 Centre Avenue",
            "address2": "961",
            "postCode": "15213",
            "city": "Pittsburgh",
            "state": "Pennsylvania",
            "foodType": "Chinese",
            "date": "20190310",
            "coordinates": {"lat": 40.4537737, "lng": -79.9431412},
        },
        {
            "owner": "dummy2",
            "postID": 3,
            "address1": "4716 Ellsworth Avenue",
            "address2": "11",
            "postCode": "15213",
            "city": "Pittsburgh",
            "state": "Pennsylvania",
            "foodType": "Korean",
            "date": "20190310",
            "coordinates": {"lat": 40.4483713, "lng": -79.946681},
        },
        {
            "owner": "dummy3",
            "postID": 4,
            "address1": "2 Bayard Road",
            "address2": "23",
            "postCode": "15213",
            "city": "Pittsburgh",
            "state": "Pennsylvania",
            "foodType": "Japanese",
            "date": "20190210",
            "coordinates": {"lat": 40.4534333, "lng": -79.9427924},
        },
        {
            "owner": "dummy4",
            "postID": 5,
            "address1": "5000 Forbes Avenue",
            "address2": "11",
            "postCode": "15213",
            "city": "Pittsburgh",
            "state": "Pennsylvania",
            "foodType": "Japanese",
            "date": "20190422",
            "coordinates": {"lat": 40.44416469999999, "lng": -79.9433725},
        },
        {
            "owner": "dummy6",
            "postID": 6,
            "address1": "239 ATWOOD ST",
            "postCode": "15213",
            "city": "Pittsburgh",
            "state": "Pennsylvania",
            "foodType": "Japanese",
            "date": "20190402",
            "coordinates": {"lat": 40.44036937, "lng": -79.95628244},
        },
        {
            "owner": "dummy7",
            "postID": 7,
            "address1": "3609 FORBES AV",
            "postCode": "15213",
            "city": "Pittsburgh",
            "state": "Pennsylvania",
            "foodType": "Japanese",
            "date": "20190322",
            "coordinates": {"lat": 40.44076978, "lng": -79.95815268},
        },
        {
            "owner": "dummy8",
            "postID": 8,
            "address1": "3800 FIFTH AV",
            "postCode": "15213",
            "city": "Pittsburgh",
            "state": "Pennsylvania",
            "foodType": "Japanese",
            "date": "20190329",
            "coordinates": {"lat": 40.44234167, "lng": -79.9574109},
        },
        {
            "owner": "dummy9",
            "postID": 9,
            "address1": "214 OAKLAND AV",
            "postCode": "15213",
            "city": "Pittsburgh",
            "state": "Pennsylvania",
            "foodType": "American",
            "date": "20190307",
            "coordinates": {"lat": 40.44082789, "lng": -79.95568695},
        },
        {
            "owner": "dummy10",
            "postID": 10,
            "address1": "3907 FORBES AV",
            "postCode": "15213",
            "city": "Pittsburgh",
            "state": "Pennsylvania",
            "foodType": "Italian",
            "date": "20190305",
            "coordinates": {"lat": 40.44223638, "lng": -79.95616413},
        },
    ]
    return posts


def getRandomRecs(num_recs=5, exclude=None):
    posts = getDummyPosts()
    rand_recs = recommendAtRandom(posts, num_recs, exclude)
    return rankRecs(rand_recs, "date")


def getUserRecs(username, num_recs=5):
    posts = getDummyPosts()
    history = getUserHistory(username, posts)
    preferred = getUserPreference(username, history)
    recs_by_food_type = recommendByFoodType(posts, preferred, history)
    if len(recs_by_food_type) < num_recs:
        exclude = recs_by_food_type + history
        rand_recs = getRandomRecs(num_recs - len(recs_by_food_type), exclude)
    recs_by_food_type.extend(rand_recs)
    return rankRecs(recs_by_food_type, "date")


def getUserHistory(username, posts):
    user_history = list(filter(
        lambda post: post['owner'] == username or username in post['guest'], posts))
    return user_history


def getUserPreference(username, history):
    counter = {}
    for post in history:
        foodType = post['foodType']
        counter[foodType] = counter.get(foodType, 0) + 1
    sorted_counter = sorted(
        counter.items(), key=operator.itemgetter(1))
    sorted_counter.reverse()
    if sorted_counter is None or len(sorted_counter) == 0:
        return []
    return sorted_counter[0][0]


if __name__ == "__main__":

    getUserRecs("yuyanj143")
    # print(getUserRecs("kaidiz"))
    # print()
    # print(getRandomRecs())

    # posts = getDummyPosts()

    # recs_by_food_type = recommendByFoodType(posts, "Chinese", None)
    # print(rankRecs(recs_by_food_type, "loc", 40.44041241, -79.95519815))
    # print()
    # print(rankRecs(recs_by_food_type, "date"))

    # print()

    # rand_recs = recommendAtRandom(posts)
    # print(rankRecs(rand_recs, "loc", 40.44041241, -79.95519815))
    # print()
    # print(rankRecs(rand_recs, "date"))

    # print()


# def naive_cipher(s, slip=3):
#     res = ''
#     for i in range(len(s)):
#         char = s[i]
#         res += chr((ord(char) + slip))
#     return res
