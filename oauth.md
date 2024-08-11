# Setting up OAuth flow

The app uses Oauth with Google to get the access to dataportability API. This API allows the app to fetch the saved links and items in their account which is further processed to get the restaurants saved.

## Get OAuth Credentials
This document shows you how to setup and get oauth credentials: https://support.google.com/cloud/answer/6158849?hl=en

Once you get the credentials, you would need to pick up `client_id`, `client_secret` and fill in the `AuthenticationService.kt`. 

## Get verified deep link
This app relies on a valid redirect uri that works as deep link too for the app. This allows the redirected page to be captured by the App. For this you will need to make sure that you setup the redirect uri which is publicly accessible domain and verified: https://developer.android.com/training/app-links/verify-android-applinks

And then update the same file `AuthenticationService.kt` with your redirect uri and in the manifest to set up the intent filter to capture your redirect url.
