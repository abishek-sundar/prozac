# prozac

Prozac(also the name of an antidepressant) is an app which connects you to select people(whom you choose) based on if you are feeling depressed or anxious. It is a tool designed to mitigate suicide and depression by connecting people during times of need. The app goes through the messages the user has sent and runs IBM's sentinent analysis on them. If it is identified that the user requires help, the contacts whom they have chosen will be notified about the user's current state through Android Deep Links sent by SMS and let them know what they can do to help the user.

##Adding contacts
Trusted contacts can be added by clicking on the Add contacts button on the home page which redirects to the Contacts App where you can tap on the name of a person to add their details to the SQLite Database of the app. This database is later used to send the DeepLinks mentioned below.

##Sending DeepLinks
DeepLinks are custom links which can be opened only by people who owm the app. These DeepLinks are sent from the user to their trusted contacts using SMS when IBM Watson's sentiment analysis determines that the recent messages they sent suggests need for treatment. The link redirects to an App page which shows basic information on next steps and advice on helping the user.