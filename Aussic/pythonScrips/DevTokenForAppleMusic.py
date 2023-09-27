import time
import jwt

team_id = "798ZU7QTV7"
key_id = "2M3TACV243"


with open("/Users/yz.gao/Downloads/AuthKey_2M3TACV243.p8", "r") as key_file:
    private_key = key_file.read()


current_time_in_seconds = int(time.time())


six_months_in_seconds = 15777000
expiration_time_in_seconds = current_time_in_seconds + six_months_in_seconds


claims = {
    "iss": team_id,
    "iat": current_time_in_seconds,
    "exp": expiration_time_in_seconds,
    "alg": "ES256",
    "kid": key_id
}


developer_token = jwt.encode(claims, private_key, algorithm='ES256', headers={"kid": key_id})

print("Developer Token:", developer_token)


## curl -v -H 'Authorization: Bearer eyJhbGciOiJFUzI1NiIsImtpZCI6IjJNM1RBQ1YyNDMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiI3OThaVTdRVFY3IiwiaWF0IjoxNjk1ODAyMjQ2LCJleHAiOjE3MTE1NzkyNDYsImFsZyI6IkVTMjU2Iiwia2lkIjoiMk0zVEFDVjI0MyJ9.xIinOhP0p2a_ucOb4qMvGaQDNiVcJ00DGN4oSMOBEhz-VGim5VT1UK8sh02ncnMkuf5ZVaH3rK_Yc7Oak94TiA' "https://api.music.apple.com/v1/test"
