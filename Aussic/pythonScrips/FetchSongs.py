import requests
import json

australianArtists = []
##search songs by artist via API
def searchByArtist(artist):

    with open("backup/" + artist + ".json", "w") as json_file:
        json.dump([], json_file)

    url = "https://api.music.apple.com/v1/catalog/us/search"
    headers = {
        "Authorization": "Bearer eyJhbGciOiJFUzI1NiIsImtpZCI6IjJNM1RBQ1YyNDMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiI3OThaVTdRVFY3IiwiaWF0IjoxNjk0OTk2NTAzLCJleHAiOjE3MTA1NDg1MDMsImFsZyI6IkVTMjU2Iiwia2lkIjoiMk0zVEFDVjI0MyJ9.B5TstbUNf958j_HGA_nlW62JI5i6sOexFdrLpnwaAeJZxpyPfifSOqMsse8JOxkhQiUB4sjpWbVKMA6yvPfXdw"
    }
    params = {
        "limit" : 5,
        "offset" : 0,
        "term" : artist,
        "types":"artists"
    }


    res = requests.get(url = url, headers = headers, params = params).json()
    print("Artists" +str(res)) ##########
    albums = res["results"]["artists"]["data"][0]["relationships"]["albums"]["data"]
    for album in albums:
        songs = requests.get(url = "https://api.music.apple.com" + album["href"], headers = headers).json()["data"][0]["relationships"]["tracks"]["data"]
        with open('../app/src/main/java/au/edu/anu/Aussic/models/entity/songs.json', 'r', encoding='utf-8') as json_file:
            data = json.load(json_file)
        for song in songs:
            data.append(song)
        print("songs in album " + album + " saved") ##########
        with open("../app/src/main/java/au/edu/anu/Aussic/models/entity/songs.json", "w") as json_file:
            json.dump(data, json_file)

        ##backup
        with open("backup/" + artist + ".json", 'r', encoding='utf-8') as json_file:
            backupData = json.load(json_file)
        for song in songs:
            backupData.append(song)
        with open("backup/" + artist + ".json", "w") as json_file:
            json.dump(backupData, json_file)
        ##write logfile
        with open("log.txt", "a") as log:
            log.write("Artist: " + artist + " Album: " + str(album) + " saved \n")




searchByArtist("The+Necks")
