# DEPLOYMENT TO GCP

This is several steps to deploy our backend system to Google Cloud Platform (GCP)

# GCP Architecture
<img src="https://github.com/ItsMeMan221/animal-snap/blob/main/docs/architecture.png" alt="GCP Architecture">


# Set the env variables and enable some services

```bash
export PROJECT_ID=$(gcloud config get-value project)
export PROJECT_NUMBER=$(gcloud projects describe $PROJECT_ID --format='value(projectNumber)')
export REGION=asia-southeast2
```


# Setup Google Cloud Storage

1. Make a bucket
```bash
gsutil mb -p $PROJECT_ID -l $REGION -b on gs://animal-snap-bucket
```
2. Give allUser access to view the bucket
```bash
gsutil iam ch allUsers:objectViewer gs://animal-snap-bucket
```
3. Create a service account for cloud run to create/delete/view object
```bash
gcloud iam service-accounts create animal-snap-object-admin

export SERVICE_ACCOUNT_EMAIL=$(gcloud iam service-accounts describe animal-snap-object-admin@$PROJECT_ID.iam.gserviceaccount.com --format='value(email)')

gcloud projects add-iam-policy-binding $PROJECT_ID --member=serviceAccount:$SERVICE_ACCOUNT_EMAIL --role=roles/storage.admin

```
4. Generate a key to json format

```bash
gcloud iam service-accounts keys create credentials.json --iam-account=$SERVICE_ACCOUNT_EMAIL
```
5. Upload .sql format from <a href="https://github.com/ItsMeMan221/animal-snap/blob/main/docs/animal-snap.sql">here</a> to your bucket!


# Setting up SQL instance

1. Create VPC Access
```bash
gcloud compute networks vpc-access connectors create animal-snap-connector --region=$REGION --network=default --range=10.8.0.0/28 --min-instances=2 --max-instances=3 --machine-type=f1-micro
```
2. Reserve an IP
```bash
gcloud compute addresses create animal-snap-ip \
    --global \
    --purpose=VPC_PEERING \
    --prefix-length=20 \
    --network=projects/$PROJECT_ID/global/networks/default
```

3. Create Private VPC
```bash
gcloud services vpc-peerings connect \
    --service=servicenetworking.googleapis.com \
    --ranges=animal-snap-ip \
    --network=default \
    --project=$PROJECT_ID
```

4. Create SQL instance (MySQL) through console

| Variable         | Value                                                |
| :---------------:| :---------------------------------------------------:|
| Instance Id      | animal-snap-instance                                 |
| Password         | mycapstoneprod                                       |
| Database version | MySQL 8.0                                            |
| Region           | asia-southeast2                                      |
| Machine Type     | Lightweight 1vCPU, 3.75GB                            |
| Connection       | Check private IP, default network, uncheck public IP |
| Storage          | HDD, 20 GB Capacity, Uncheck storage increase        |

5. Create database
```bash
gcloud sql databases create animal-snap --instance=animal-snap-instance
```

6. Import .sql
    - Click on your newly created instance to see it's detail
    - Note the IP, it will be used later
    - Click on import button on top side menu
    - Click browse, then select the bucket and select .sql file
    - On destination section, select database to ``` animal-snap ```

# Cloud Run deployment

1. Clone our repositories
``` bash
git clone https://github.com/ItsMeMan221/animal-snap.git

cd animal-snap/'Cloud Computing'
```

2. Configure several files
    - Move the generated key earlier to Cloud Computing folder
    ```bash
    mv ~/credentials.json ~/animal-snap/'Cloud Computing'
    ```
    - Create a app.yaml files
    ```bash
    touch app.yaml

    nano app.yaml

    env_variables:
        JWT_SECRET_KEY: "6dcc9951a30568db5f221e3ee7e8c455"
        SQLALCHEMY_DATABASE_URI: "mysql+pymysql://root:mycapstoneprod@["YOUR_PRIVATE_IP"]/animal-snap"
    ```
    Replace the ["YOUR_PRIVATE_IP"] with SQL private IP that you note earlier!

    - Move the model from machine learning folder to cloud computing folder
    ```bash
    mv ~/animal-snap/'Machine Learning'/model_14class_v3.h5 ~/animal-snap/'Cloud Computing'/model.h5
    ```
    - Edit the modules/bucket_user
    ```bash
    nano modules/bucket_user.py

    bucket = client.get_bucket('animal-snap-buckets')
    ```
    - Create a .dockerignore file
    ```bash
    touch .dockerignore

    nano .dockerignore

    Dockerfile
    README.md
    *.pyc
    *.pyo
    *.pyd
    __pycache__
    .pytest_cache
    ```
3. Create Artifact Registry
```bash
gcloud artifacts repositories create animalsnap \
    --location=$REGION \
    --repository-format=docker
```
4. Build docker image
```bash
docker build -t docks/animalsnap:v1 .
```
5. Push the docker image to artifact registry
```bash
docker tag docks/animalsnap:v1 asia-southeast2-docker.pkg.dev/$PROJECT_ID/animalsnap/animalsnap:v1

docker push asia-southeast2-docker.pkg.dev/$PROJECT_ID/animalsnap/animalsnap:v1
```
Important note: if you get an error artifactregistry.repositories.uploadArtifacts" denied use this following command

```bash
gcloud auth configure-docker asia-southeast2-docker.pkg.dev
```

6. Deploy to cloud run
```bash
gcloud run deploy animalsnap \
--image=asia-southeast2-docker.pkg.dev/$PROJECT_ID/animalsnap/animalsnap:v1 \
--region=$REGION \
--min-instances=0 \
--max-instances=4 \
--port=8080 \
--vpc-connector=animal-snap-connector \
--memory=2Gi \
--cpu=2
```