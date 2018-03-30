import googlemaps
import json


def distanceCalcaultor(origin, destination):
    f = open('distance.json','w')
    maps = googlemaps.Client('AIzaSyCFreEK4Ur8T7aV3CRG7pwSbKvfaT89YpQ')

    my_distance = maps.distance_matrix(str(origin), str(destination))
    d = (str(my_distance))
    d2 = json.dumps(d)
    f.write(str(d2))
    f.close()


distanceCalcaultor('Galway','Dublin')