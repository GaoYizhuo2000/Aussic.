import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import json

cred = credentials.Certificate("aussic-52582-firebase-adminsdk-gc3dc-1bf9916ac2.json")
firebase_admin.initialize_app(cred)
db = firestore.client()

with open('../app/src/main/java/au/edu/anu/Aussic/models/entity/songs.json', 'r', encoding='utf-8') as json_file:
    data = json.load(json_file)

for i in range(len(data)):
    song = data[i]
    songId = song['id']
    songRef = db.collection('songs').document(songId)
    songRef.set(song)
    print("uploaded " + str(i))

