
#  Flow for Mobile Android SDK

![BETA](https://img.shields.io/badge/status-BETA-yellow)
# 📱 Sample Android App – Flow SDK Integration Demo

This sample Android project demonstrates how to integrate the `Flow SDK` into your application. It showcases the basic setup, initialisation, and usage of key features provided by the SDK.
  
---  

## 🚀 Features Demonstrated

- SDK initialisation
- Basic usage of core SDK components
- Minimal UI integration example

---  

## 📦 Requirements

- Android Studio **Ladybug or newer**
- Gradle **8.x**
- Android SDK **minSdk 21+, targetSdk 35**
- Kotlin **1.9+**

---  

## ⚙️ Getting Started

1. **Clone the repo**
 ```bash  
  git clone https://github.com/checkout/{the_final_url_once_public}.git
  ```

2. **Sync Gradle & Build the Project**

3. **Add you configuration in the local.properties file**

We set up the project in order to look for the following keys in you local.properties
Please use our [Dashboard](https://identity-sandbox.checkout.com/oauth2/aus1iz81iz5DvxYuE0h8/v1/authorize?client_id=0oa1iz7yoxvcL8hrs0h8&code_challenge=vj7IRrecnOC_FCphGk3kgNdUMj31w7RJLRerbYyzV8M&code_challenge_method=S256&nonce=6FhXKSwc4YMnK1JuxFkdKJubhqcVXZu4hnf6fSTn4DQXLk2AAhUk1SVL3CY1cTJ8&redirect_uri=https%3A%2F%2Fdashboard.sandbox.checkout.com%2Fimplicit%2Fcallback&response_type=code&state=puwcYw4XTvYWTXC50kha7prf1cvl3uFfJ2x8oNGO5xMVOztfWVs1yNblI5Y7YR81&scope=openid%20email%20profile) to create those values and add them as per the following
```
sandbox.components.processing_channel= {YOUR_PROCESSING_CHANNEL}
sandbox.components.secret_key= {YOUR_SECRET_KEY}
sandbox.components.public_key= {YOUR_PUBLIC_KEY}
```
The values will be then available as Build.Config across the project

## 📘 SDK Integration Walkthrough

The integration is broken down into clearly marked steps in the codebase — just look for comments like `// STEP 1`, `// STEP 2`, etc.

Here’s a quick overview of what each step does and where to find it:

| Step       | Description                             | File / Location                |
|------------|-----------------------------------------|-------------------------------|
| `// STEP 1` | Create the payment session      | `SampleViewModel.kt` |
| `// STEP 2` | Create the CheckoutComponent     | `SampleViewModel.kt`             |
| `// STEP 3` | Create the configuration needed for the factory | `SampleViewModel.kt`                |
| `// STEP 4` | Create the component factory | `SampleViewModel.kt`       
| `// STEP 5` | Create the component | `SampleViewModel.kt`       
| `// STEP 6` | Use the component in your UI | `SampleViewModel.kt`,`SampleScreen.kt`       
| `// STEP 7` | Create a GooglePayFlowCoordinator | `SampleViewModel.kt`,`SampleActivity.kt`       


Each step includes inline comments explaining what's happening and why.

> 💡 **Pro tip**: Search the codebase for `STEP` to jump between sections quickly.


👉 For full SDK documentation, check: [Flow SDK Docs](https://www.checkout.com/docs/payments/accept-payments/accept-a-payment-on-your-mobile-app/get-started-with-flow-for-mobile)
👉 For full SDK reference, check: [Flow Mobile Library reference](https://www.checkout.com/docs/payments/accept-payments/accept-a-payment-on-your-mobile-app/flow-for-mobile-library-reference/android)



**⚠️ ## Notes / Limitations** <br>
-   This demo uses a simplified architecture for clarity.
-   Error handling and edge cases are intentionally minimal.
-   Please do not use this as a production app template.