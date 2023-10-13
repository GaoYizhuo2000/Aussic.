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
        artists[song["attributes"]["artistName"]] = song["attributes"]["artwork"]["url"]
 #   genreNames =  song["attributes"]["genreNames"]
 #   for genreName in genreNames:
 #       if genreName not in genres:
 #           genres[genreName] = []
 #           genres[genreName].append(song["id"])
 #       else:
 #           genres[genreName].append(song["id"])
    print(i)
    i+=1

##
##for i in artists:
##    if "/" in i:
##        continue
##    db.collection('artists').document(str(i)).set({i:artists[i]})
##    print("uploaded artist  " + i)
##for i in genres:
##    if "/" in i:
##        continue
##    db.collection('genres').document(str(i)).set({i:genres[i]})
##    print("uploaded genre  " + i)





#增加新参数
for i in artists:
    if "/" in i:
        continue
    newAttr = {
        "url": artists[i]
    }
    db.collection('artists').document(i).update(newAttr)
    print("updated artist  " + i)



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
##collection = db.collection('genres')
##docs = db.collection('genres').stream()
##newAttr = {
##    "type": "genres"
##}
##i = 0
##for doc in docs:
##    docRef = collection.document(doc.id)
##    docRef.update(newAttr)
##    i +=1
##    print("updated "  + str(i)+ doc.id)




