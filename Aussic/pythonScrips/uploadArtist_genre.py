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
genres = {}
i = 1
for song in data:
    if song["attributes"]["artistName"] not in artists:
        artists[song["attributes"]["artistName"]] = []
        artists[song["attributes"]["artistName"]].append(song["id"])
    else:
        artists[song["attributes"]["artistName"]].append(song["id"])
    genreNames =  song["attributes"]["genreNames"]
    for genreName in genreNames:
        if genreName not in genres:
            genres[genreName] = []
            genres[genreName].append(song["id"])
        else:
            genres[genreName].append(song["id"])
    print(i)
    i+=1

for i in artists:
    if "/" in i:
        continue
    db.collection('artists').document(str(i)).set({i:artists[i]})
    print("uploaded artist  " + i)
for i in genres:
    if "/" in i:
        continue
    db.collection('genres').document(str(i)).set({i:genres[i]})
    print("uploaded genre  " + i)





#增加新参数
##collection = db.collection('songs')
##docs = db.collection('songs').stream()
##newAttr = {
##    "likes": 0,
##    "favorites": 0,
##    "comments": {"num":1, "details":[{"uid": "nerogao777@gmail.com", "content":"Wonderful!"}]}
##}
##i = 0
##for doc in docs:
##
##    docRef = collection.document(doc.id)
##    batch.update(docRef, newAttr)
##    i +=1
##    print("updated  " + str(i))



