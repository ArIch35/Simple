import time

from selenium import webdriver
from selenium.webdriver import Keys
from selenium.webdriver.firefox.options import Options

#const
SOURCE_FILE = "input.txt"
PATH = r'C:\Users\moses\AppData\Roaming\Mozilla\Firefox\Profiles\mm37urk0.default-release'
WEB_URL = "https://google.com"

def get_options():
    options = Options()
    options.add_argument("-profile")
    options.add_argument(PATH)
    return options

def get_query_length(query):
    return str(query).__sizeof__()

def generate_backspace_keys(length):
    temp = ""
    for i in range(length):
        temp += Keys.BACKSPACE
    return temp

def search_google():
    prev_query_length = get_query_length(
        driver.find_element('xpath', "/html/body/div[4]/div[2]/form/div[1]/div[1]/div[2]/div/div[2]/input").text)
    driver.find_element('xpath', "/html/body/div[4]/div[2]/form/div[1]/div[1]/div[2]/div/div[2]/input").send_keys(
        generate_backspace_keys(prev_query_length) + input("enter: ") + Keys.ENTER)

#main
driver = webdriver.Firefox(options=get_options())
driver.get(WEB_URL)
driver.find_element('name', 'q').send_keys(input("enter: ") + Keys.ENTER)
time.sleep(0.1)
while True:
    search_google()
    time.sleep(0.1)


