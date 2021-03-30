# SmartRemoteController
This app is smart remote controller app where user can control devices in his/her house such as
TVs and ACs from their mobile regardless of the location they are at. It allows user to add as
many devices as they want.

#How the app works
The app works by connecting to a database to retrieve saved devices and display list on the
phone user can add, remove and edit device or button name, when a user click on a device
from the displayed devices it displays the list of buttons of this device. When a button is clicked
the app writes the IR function code of this particular device in the database where the
nodeMCU can read and send this code via IR to perform the function. When adding a new
button user puts nodeMCU in receiving mode so it can receive IR code from the device remote
controller then user enters buttons name press ok then press the button he wants to add from
device remote controller so that the app can read code from database and save it.
