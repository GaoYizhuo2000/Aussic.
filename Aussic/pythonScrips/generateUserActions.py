import json
import random
import string


def randomEmail():
    email_domains = ["gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "icloud.com"]
    characters = string.ascii_letters + string.digits
    username =  ''.join(random.choice(characters) for _ in range(8))
    random_domain = random.choice(email_domains)
    return username + "@" + random_domain

def randomSong():
    with open('../pythonScrips/backup/songs.json', 'r', encoding='utf-8') as json_file:
        data = json.load(json_file)
        song = random.choice(data)

        return {"targetSongId": song["id"], "targetSong": song["attributes"]["name"]}

def randomContent():
    return random.choice(["Nice!", "Good!!", "I like this song so much", "Wonderful!"])

def generateLikeData():
    data = {
        "actionType": "like",
        "userName": randomEmail(),
        "targetSong": randomSong()["targetSong"],
        "targetSongId": randomSong()["targetSongId"],
        }
    return data

def generateFavoriteData():
    data = {
        "actionType": "favorite",
        "userName": randomEmail(),
        "targetSong": randomSong()["targetSong"],
        "targetSongId": randomSong()["targetSongId"],
    }
    return data
def generateCommentData():
    data = {
        "actionType": "comment",
        "userName": randomEmail(),
        "targetSong": randomSong()["targetSong"],
        "targetSongId": randomSong()["targetSongId"],
        "content": randomContent()
        }
    return data
    
    
userActionList = []
functionList = ['generateLikeData', 'generateCommentData', "generateFavoriteData"]

for i in range(2500):
    function = random.choice(functionList)
    if function == 'generateLikeData':
        userActionList.append(generateLikeData())
    if function == 'generateCommentData':
        userActionList.append(generateCommentData())
    if function == 'generateFavoriteData':
        userActionList.append(generateFavoriteData())
    print(i)
    


with open("../app/src/main/res/raw/useractions.json", "w") as json_file:
    json.dump(userActionList, json_file)
