# StudyBitsWallet

[![CircleCI](https://circleci.com/gh/tijn167/StudyBitsWalletClone/tree/master.svg?style=svg)](https://circleci.com/gh/tijn167/StudyBitsWalletClone/tree/master) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=tijn167_StudyBitsWalletClone&metric=bugs)](https://sonarcloud.io/dashboard?id=tijn167_StudyBitsWalletClone) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=tijn167_StudyBitsWalletClone&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=tijn167_StudyBitsWalletClone)


Android Wallet application for the StudyBits project. This allows students to receive and prove their credentials.

Works in conjunction with the [StudyBits Agent](https://github.com/Quintor/StudyBits)

## Building

1. Import the project in Android Studio
2. Execute the wanted gradle task. (e.g. build)

## Running
1. Run the docker compose setup from the university-agent (located at `./StudyBits/`) (injecting your ip)
2. Create a file `gradle.properties` containing `ENDPOINT_IP="<YOUR_IP_HERE>"`
3. Start an emulator or connect an android device to your machine
4. Build and deploy to a device

## Running tests
1. Follow the steps above
2. Run `./gradlew connectedCheck` (or run the corresponding task in Android Studio)
