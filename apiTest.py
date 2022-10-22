import googlemaps
from datetime import datetime
import geocoder
import json
from random import randint

# entering the location name
g = geocoder.ip('me')
#print(g.latlng)

gmaps = googlemaps.Client(key='AIzaSyD0e_AjT0xXkIjPu3VswknGPL62DVSp4oI')

find_result = gmaps.places_nearby(g.latlng ,radius=500, type="hospital")
find_result = find_result["results"]
dist_dict = {}
for i in range(len(find_result)):
    try:
        open_now = find_result[i]["opening_hours"]["open_now"]
    except KeyError:
        pass
    if (not open_now):
        continue
    name = find_result[i]["name"]
    dist_matrix = gmaps.distance_matrix(origins=tuple(g.latlng), destinations=name, mode="driving")
    travel_time = dist_matrix["rows"][0]["elements"][0]["duration"]["text"]
    travel_time = int(travel_time.split()[0])
    #dummy wait time
    wait_time = randint(1, 25)
    total_time = wait_time + travel_time
    time_list_temp = [travel_time, wait_time, total_time]
    dist_dict[name] = time_list_temp
#add functionality to check if hospital is actually open now with "open_now" attribute
dist_dict = sorted(dist_dict.items(), key=lambda x: x[1][2])
print(dist_dict)

