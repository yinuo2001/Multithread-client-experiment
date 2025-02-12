import pandas as pd
import numpy as np

for i in range(10, 40, 10):
    dataJava = pd.read_csv(f'/Users/nuanxin/Desktop/NEU/6650/hw4aClient/src/main/resources/output{i}.csv')
    # Calculate the mean of the data if the request is Get
    meanGetJava = dataJava[dataJava['request'] == 'GET']['responseTime'].mean()
    # Calculate the max of the data if the request is Get
    maxGetJava = dataJava[dataJava['request'] == 'GET']['responseTime'].max()
    # Calculate the min of the data if the request is Get
    minGetJava = dataJava[dataJava['request'] == 'GET']['responseTime'].min()
    # Calculate the 50th percentile of the data if the request is Get
    percentile90GetJava = np.percentile(dataJava[dataJava['request'] == 'GET']['responseTime'], 50)
    # Calculate the 99th percentile of the data if the request is Get
    percentile99GetJava = np.percentile(dataJava[dataJava['request'] == 'GET']['responseTime'], 99)

    # Calculate the mean of the data if the request is Post
    meanPostJava = dataJava[dataJava['request'] == 'POST']['responseTime'].mean()
    # Calculate the max of the data if the request is Post
    maxPostJava = dataJava[dataJava['request'] == 'POST']['responseTime'].max()
    # Calculate the min of the data if the request is Post
    minPostJava = dataJava[dataJava['request'] == 'POST']['responseTime'].min()
    # Calculate the 50th percentile of the data if the request is Post
    percentile90PostJava = np.percentile(dataJava[dataJava['request'] == 'POST']['responseTime'], 50)
    # Calculate the 99th percentile of the data if the request is Post
    percentile99PostJava = np.percentile(dataJava[dataJava['request'] == 'POST']['responseTime'], 99)


    print(f'Mean of response time for GET request in 10, {i}, 2 Java: ', meanGetJava)
    print(f'Max of response time for GET request in 10, {i}, 2 Java: ', maxGetJava)
    print(f'Min of response time for GET request in 10, {i}, 2 Java: ', minGetJava)
    print(f'50th percentile of response time for GET request in 10, {i}, 2 Java: ', percentile90GetJava)
    print(f'99th percentile of response time for GET request in 10, {i}, 2 Java: ', percentile99GetJava)

    print(f'Mean of response time for POST request in 10, {i}, 2 Java: ', meanPostJava)
    print(f'Max of response time for POST request in 10, {i}, 2 Java: ', maxPostJava)
    print(f'Min of response time for POST request in 10, {i}, 2 Java: ', minPostJava)
    print(f'50th percentile of response time for POST request in 10, {i}, 2 Java: ', percentile90PostJava)
    print(f'99th percentile of response time for POST request in 10, {i}, 2 Java: ', percentile99PostJava)
    