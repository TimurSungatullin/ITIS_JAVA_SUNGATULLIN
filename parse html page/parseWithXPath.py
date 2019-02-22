from lxml import html
import MySQLdb
import time
import requests

headers = {
    'Accept': 'Changes/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
    'Accept-Language': 'ru',
    'Accept-Encoding': 'br, gzip, deflate',
    'Connection': 'keep-alive',
    'DNT': '1',
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) '
                  'Chrome/61.0.3163.100 Safari/537.36',
}

URL = 'http://hrumi.ru'
PAGE = 'page/'
products = []


def normalise_ref(ref):
    if ref[0] == "/":
        if ref[1] == "/":
            ref = "http:" + ref
        else:
            ref = URL + ref
    return ref


'''
    Download html page 
    If the status code is 404, 
    then such a page does not exist 
    and it is necessary to exit the recursion.
'''
def download(url):
    time.sleep(1)
    response = requests.get(url, headers=headers)
    text = response.text
    if response.status_code == 200:
        return text
    else:
        return -1


# Parse page with categories
def parse_cat(text):
    tree = html.fromstring(text)
    catalog_list = tree.xpath('//ul[contains(@class, \"products\")]/li[contains(@class, \"category\")]/a/@href')
    return catalog_list


class Product:
    def __init__(self, title, price, ref_product, ref_img):
        self.title = title
        self.price = price
        self.ref_product = ref_product
        self.ref_img = ref_img


# parse one page with products
def parse(text):
    tree = html.fromstring(text)
    buf = []
    catalog_list = tree.xpath('//ul[contains(@class, \"products\")]/li')
    for q in catalog_list:
        ref_img = q.xpath('.//img/@src')[0]
        ref_img = normalise_ref(ref_img)
        title = q.xpath('.//h2/text()')[0]
        ref_product = q.xpath('.//a[contains(@class, \"LoopProduct\")]/@href')[0]
        ref_product = normalise_ref(ref_product)
        price = q.xpath('.//span[contains(@class, \"amount\")]/text()')[0]
        buf.append(Product(title, price, ref_product, ref_img))
    return buf


# Recursive page search
def rec_parse(ref_cat, i=0):
    i += 1
    f = download(ref_cat + PAGE + str(i))
    if f == -1:
        return
    b = parse(f)
    if len(b) != 0:
        products.extend(b)
    rec_parse(ref_cat, i)


html_doc = download(URL)
categories = parse_cat(html_doc)

# Categories search
for category in categories:
    rec_parse(category)

# Record in DB
conn = MySQLdb.connect('localhost', 'root', 'qtimur99', 'db1')
cursor = conn.cursor()
try:
    for product in products:
        query = ("INSERT INTO db1.product (title, price, ref_img, ref_product) VALUE (\"%s\", \"%s\", \"%s\", \"%s\");"
                 % (product.title, product.price, product.ref_img[:20], product.ref_product[:20]))
        cursor.execute(query)
except MySQLdb.Error as e:
    print("MySQL Error [%d]: %s" % (e.args[0], e.args[1]))
conn.autocommit(on=1)
conn.close()
