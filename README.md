
# MaPi

## Overview

Do you constantly add new restaurants and cafés to your Google Maps saved places without really trying them out? 
Or you can't find the correct place from your saved places when you're craving something? 
If that's the case then we have the perfect solution for you!
You just need to login to your google account and tell us about your current craving. 
We will look into your saved places and find the best place for you! 


## Screenshots

| **Welcome Screen** | **User prompt** |
| -------------- | ----------- |
| ![mapi_initial](https://github.com/user-attachments/assets/1567897c-2e9a-466c-b082-0d1e21abbd0f) | ![mapi_result](https://github.com/user-attachments/assets/2e89efa4-c57c-480e-909b-e668020dfbc3) |

## Installation

### Setup

1. **Clone the Repository**:
    
    `git clone https://github.com/mahyami/mapi`

2. **Create your API keys**:
    Go to Google [consoles](https://console.cloud.google.com/apis/credentials).
    Create the API Key for Maps
<img width="1502" alt="google_console" src="https://github.com/user-attachments/assets/f4e16c02-89c5-4677-aa27-e13e21b838dd">

And then create one for Gemini as well here: ```https://aistudio.google.com/app/apikey```

4. **Environment Variables**:
    - Create a `apikey.properties` file in the root directory.
	    ```
	    GOOGLE_API_KEY="YOUR_KEY"  
		GOOGLE_GEN_AI_KEY = "YOUR_KEY"
	    ```
    - Replace the placeholders with your actual API keys.
5. **OAuth**:
    Please refere to [this](https://github.com/mahyami/mapi/blob/main/oauth.md) doc.

