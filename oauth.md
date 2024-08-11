# Setting up OAuth flow

The app uses Oauth with Google to get the access to dataportability API. This API allows the app to fetch the saved links and items in their account which is further processed to get the restaurants saved.

## Get OAuth Credentials
This document shows you how to setup and get oauth credentials: https://support.google.com/cloud/answer/6158849?hl=en

Once you get the credentials, you would need to pick up `client_id`, `client_secret` and fill in the `AuthenticationService.kt`. 

You will also need to make sure, you setup the correct redirect uri, which is publicly accessible domain and verified: https://developer.android.com/training/app-links/verify-android-applinks

And then update the same file `AuthenticationService.kt` with your redirect uri and in the manifest to set up the intent filter to capture your redirect url.