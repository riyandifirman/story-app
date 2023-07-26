# Story App
My Story App | Submission of Intermediate Android | Bangkit 2023 Series

An application built in the learning process of Bangkit Academy 2023. This is a practical implementation in the class Learning Intermediate Android App Development

## Final Submission of Story Application
### The Criteria
Features that must be present in the app:

<b> 1. Authentication Page </b> <br>
- Displays the login page to enter the application. The following input is required.
  - Email (R.id.ed_login_email)
  - Password (R.id.ed_login_password)
- Create a register page to register in the application. The following inputs are required.
  - Name (R.id.ed_register_name)
  - Email (R.id.ed_register_email)
  - Password (R.id.ed_register_password)
- Password must be hidden.
- Create a Custom View in the form of EditText on the login or register page with the following conditions.
  - If the number of passwords is less than 8 characters, display an error message directly on the EditText without having to move the form or click the button first.
- Store session data and tokens in preferences. Session data is used to manage the application flow with the following specifications.
  - If you have logged in, go directly to the main page.
  - If not, it will enter the login page. 
- There is a feature to logout (R.id.action_logout) on the main page with the following conditions.
  - When the logout button is pressed, the token, and session information must be deleted. <br>

<b> 2. List Story </b> <br> 
- Displays a list of stories from the provided API. Here is the minimum information that you must display.
  - User name (R.id.tv_item_name)
  - Photo (R.id.iv_item_photo)
- A detail view appears when one of the story items is pressed. The following is the minimum information that you must display.
  - User name (R.id.tv_detail_name)
  - Photo (R.id.iv_detail_photo)
  - Description (R.id.tv_detail_description) <br>

<b> 3. Add Story </b> <br>
- Create a page to add a new story that can be accessed from the story list page. Here are the minimum inputs required.
  - Photo file (must be from gallery)
  - Story description (R.id.ed_add_description)
- Here are the conditions for adding a new story:
  - There is a button (R.id.button_add) to upload data to the server. 
  - After the button is clicked and the upload process is successful, it will return to the story list page. 
  - The latest story data must appear at the top. <br>

<b> 4. Display Animation </b> <br>
- Create animations in the application by using one of the following animation types.
  - Property Animation
  - Motion Animation
  - Shared Element
- Write down the animation type and location in the Student Note. <br>

## Screenshot Application
<b> Splash Screen, Register & Login Page</b> <br> <br>
<img src="https://github.com/riyandifirman/story-app/assets/49358131/27c7c580-5e87-4ed4-9104-013bcebbe0ae" alt="Home Page" widht="500" height="500">
<img src="https://github.com/riyandifirman/story-app/assets/49358131/4cf18729-49ea-4acb-9b80-90a5dc190ae3" alt="Home Page" widht="500" height="500">
<img src="https://github.com/riyandifirman/story-app/assets/49358131/bd9bd654-e18c-48ef-92de-c3a7f36bef97" alt="Home Page" widht="500" height="500">

<b> Home Page </b> <br> <br>
<img src="https://github.com/riyandifirman/story-app/assets/49358131/df0a640e-153b-42a1-b24b-2bb69ac09292" alt="Home Page" widht="500" height="500">
<img src="https://github.com/riyandifirman/story-app/assets/49358131/5d1322fd-6acd-441f-948e-644700a02d55" alt="Home Page" widht="500" height="500">

<b> Add & Detail Story </b> <br> <br>
<img src="https://github.com/riyandifirman/story-app/assets/49358131/47c10ac2-7d99-4dd1-9782-e05f9d3ecf4f" alt="Home Page" widht="500" height="500">
<img src="https://github.com/riyandifirman/story-app/assets/49358131/2c15b61f-85c7-4243-99ff-9067097accad" alt="Home Page" widht="500" height="500">
