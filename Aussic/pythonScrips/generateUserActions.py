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
    with open('../app/src/main/java/au/edu/anu/Aussic/models/entity/songs.json', 'r', encoding='utf-8') as json_file:
        data = json.load(json_file)
        return random.choice(data)

def randomContent():
    return random.choice(["Nice!", "Good!!", "I like this song so much", "Wonderful!"])

def generateLikeData():
    data = {
        "actionType": "like",
        "userName": randomEmail(),
        "tagetSong": randomSong()
        }
    return data
    
def generateCommentData():
    data = {
        "actionType": "comment",
        "userName": randomEmail(),
        "tagetSong": randomSong(),
        "content": randomContent()
        }
    return data
    
    
userActionList = []
functionList = ['generateLikeData', 'generateCommentData']

for i in range(10):
    function = random.choice(functionList)
    if function == 'generateLikeData':
        userActionList.append(generateLikeData())
    if function == 'generateCommentData':
        userActionList.append(generateCommentData())
    

print(userActionList)
with open("../app/src/main/java/au/edu/anu/Aussic/models/userAction/userActions.json", "w") as json_file:
    json.dump(userActionList, json_file)
