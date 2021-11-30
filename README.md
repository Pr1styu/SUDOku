# SUDOku

## How to use the Frontend

1. Navigate to the frontend root directory
2. Open console and use these commands:
* `yarn install`
* `yarn start`
3. Until there is no complete authentication:
* Open .env and replace these: `HTTPS=TRUE` --> `HTTPS=FALSE`
* Open package.json and replace these under "scripts":\
 `"start": "set HTTPS=true&&react-scripts start"` --> `"start": "react-scripts start"`
