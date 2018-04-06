import googlemaps
import json
import sys

def distanceCalcaultor(sys.argv[0], sys.argv[1]):
    f = open('distance.json','w')
    maps = googlemaps.Client('AIzaSyCFreEK4Ur8T7aV3CRG7pwSbKvfaT89YpQ')

    my_distance = maps.distance_matrix(str(sys.argv[0]), str(sys.argv[1]))
    d = (str(my_distance))
    d2 = json.dumps(d)
    f.write(str(d2))
    f.close()

