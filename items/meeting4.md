# G06

## Team Meeting 4 - Week 8 - 28/09/2023 21:00 - 22:00

Present:

- Yizhuo Gao
- Evan Cheung
- Jiawei Niu
- Oscar Wei

Absent:

- No

Lead/scribe:

- Lead - Evan Cheung
- Scribe - Oscar Wei

## Agreed Procedure

### Stand up Procedure:

- Each team member briefly shares progress and any roadblocks
- Share discoveries about each one's research
- Go through meeting agendas
- Conclude and make decisions
- Assign tasks and set deadlines

## Agenda Items

| Number | Item                                                                     |
| :----- | :----------------------------------------------------------------------- |
| 1      | Report the current progress (product backlog).                          |
| 2      | Determine the features to be developed in the upcoming sprint.           |
| 3      | Discuss which pages need development and conduct further research later. |
| 4      | Discuss the overall color theme for the app.                             |

## Action Items

| Task                                                                           | Assigned To |  Due Date  |
| :----------------------------------------------------------------------------- | :---------: | :--------: |
| Implement the login and sign-in features.                                      |   Jiawei   | 07/10/2023 |
| Implement the like and comment parts, class "Song", backend of user activities |   Yizhuo   | 07/10/2023 |
| Design the UI, homepage, and other pages                                       |    Evan    | 07/10/2023 |
| Research on Search, parser, toenizer, and filter                               |    Oscar   | 07/10/2023 |

## Meeting Minutes


**Current Progress:**

1. **Data Crawling:** Successfully crawled data, which is stored in JSON format.
2. **Authentication:**
   - Login and sign-in are integrated with Firebase.
   - Issue with the login feature; sign up is functioning normally. The problem might be related to Firebase.
3. **Data Handling:**
   - Script for crawling data, collecting data, simulating user behavior, and managing likes and comments is in place.
4. **UI/UX:**
   - Completed the timer and homepage design.
5. **Data Structures:**
   - Finished implementing the AVL tree, including the insert and deletion functions, and tested them.

---

**Next Steps:**

1. **Search Functionality:**
   - Enhance the search function to include all entities, not just songs. This should also cover users.
2. **UI/UX Design:**
   - Draft the blueprint for every page, including the homepage.
   - Design the user page. Features to include:
     - Positioned on the far right with a label: "My".
     - Upon opening, it should display the user's name, avatar, GPS location, registration time, and favorite list.
     - Consideration: The middle plus sign can be set as a play button or it can be removed.
3. **Data Management:**
   - Utilize the AVL tree's deletion function to manage the user list.
4. **Features:**
   - Implement a "Remember Me" feature to retain the login status.
   - Allow users to select their avatar from a predefined list.
5. **Aesthetics:**
   - Color scheme: White background with red accents.

## Scribe Rotation

The following dictates who will scribe in this and the next meeting.

|    Name    |
| :---------: |
| Evan Cheung |
| Yizhuo Gao |
| Jiawei Niu |
|  Oscar Wei  |
