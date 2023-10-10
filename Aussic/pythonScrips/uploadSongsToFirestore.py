import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import json

cred = credentials.Certificate("aussic-52582-firebase-adminsdk-gc3dc-1bf9916ac2.json")
firebase_admin.initialize_app(cred)
db = firestore.client()

with open('../pythonScrips/backup/useractions.json', 'r', encoding='utf-8') as json_file:
    data = json.load(json_file)

for i in range(2500):
    useraction = data[i]
    db.collection('useractions').document(str(i)).set(useraction)
    print(i)

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



