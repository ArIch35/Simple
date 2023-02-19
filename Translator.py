import codecs
import time

from selenium import webdriver
from selenium.webdriver.firefox.options import Options

#const
SOURCE_FILE = "input.txt"
PATH = r'C:\Users\moses\AppData\Roaming\Mozilla\Firefox\Profiles\mm37urk0.default-release'
WEB_URL = "https://www.deepl.com/translator#auto/en/"

#var
text = ""
loop_num = 1

def get_options():
    options = Options()
    options.add_argument("-profile")
    options.add_argument(PATH)
    return options

def print_log(num):
    print(f"T{num}:")
    print(text.replace("%0A","\n"))

#main
driver = webdriver.Firefox(options=get_options())
print("log: ")

while True:
    prev_string = text
    with codecs.open(SOURCE_FILE, "r", 'utf-8') as input_file:
        file_as_text = input_file.read().splitlines()
        text = ""
        for line in file_as_text:
                text = text + line.replace("\n", " ").replace("/", " oder ").replace(".",".%0A%0A")

    if text != prev_string:
        driver.get(WEB_URL + text)
        print_log(loop_num)
        loop_num += 1

    time.sleep(1)


