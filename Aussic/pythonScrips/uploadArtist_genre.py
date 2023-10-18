import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import json

cred = credentials.Certificate("aussic-52582-firebase-adminsdk-gc3dc-1bf9916ac2.json")
firebase_admin.initialize_app(cred)
db = firestore.client()

with open('../pythonScrips/backup/songs.json', 'r', encoding='utf-8') as json_file:
    data = json.load(json_file)
artists = {}
genres = []
i = 1


for song in data:
##    ##if song["attributes"]["artistName"] not in artists:
##    ##    attr = {}
##    ##    attr["artistName"] = song["attributes"]["artistName"]
##    ##    attr["songs"] = []
##    ##    attr["songs"].append(song["id"])
##    ##    attr["type"] = "artists"
##    ##    attr["url"] = song["attributes"]["artwork"]["url"]
##    ##    artists[song["attributes"]["artistName"]] = attr
##    ##else:
##    ##    artists[song["attributes"]["artistName"]]["songs"].append(song["id"])
    genreNames =  song["attributes"]["genreNames"]
    print(i)
    i+=1
    for genreName in genreNames:
        if "/" in genreName:
                continue
        if genreName not in genres:
            genres.append(genreName)
db.collection('genreList').document("genreList").set({"genreList":genres})



##for i in artists:
##    if "/" in i:
##        continue
##    db.collection('artists').document(str(i)).set(artists[i])
##    print("uploaded artist  " + i)

##for i in genres:
##    if "/" in i:
##        continue
##    db.collection('genres').document(str(i)).set(genres[i])
##    print("uploaded genre  " + i)





#增加新参数
##for i in artists:
##    if "/" in i:
##        continue
##    newAttr = {
##        "url": artists[i]
##    }
##    db.collection('artists').document(i).update(newAttr)
##    print("updated artist  " + i)



##collection = db.collection('artists')
##docs = db.collection('artists').stream()
##newAttr = {
##    "type": "artists"
##}
##i = 0
##for doc in docs:
##    docRef = collection.document(doc.id)
##    docRef.update(newAttr)
##    i +=1
##    print("updated "  + str(i) + doc.id)
##
##
##collection = db.collection('users')
##docs = db.collection('users').stream()
##newAttr = {
##    "blockList": []
##}
##i = 0
##for doc in docs:
##    docRef = collection.document(doc.id)
##    docRef.update(newAttr)
##    i +=1
##    print("updated "  + str(i)+ doc.id)




