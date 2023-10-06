import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import json

cred = credentials.Certificate("aussic-52582-firebase-adminsdk-gc3dc-1bf9916ac2.json")
firebase_admin.initialize_app(cred)
db = firestore.client()

with open('../pythonScrips/backup/songs.json', 'r', encoding='utf-8') as json_file:
    data = json.load(json_file)

for i in range(1):
    song = data[i]
    song["likes"] = 0
    song["favorites"] = 0
    song["comments"] = {"num":1, "details":[{"uid": "nerogao777@gmail.com", "content":"Wonderful!"}]}
    songId = song['id']
    songRef = db.collection('songs').document(songId)
    songRef.set(song)
    print("uploaded " + str(i))

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



