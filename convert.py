from lxml import etree
from lxml import html
import os


file_nums = ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', 
             '10', '11', '12', '13', '14', '15', '16', '17', '18', '19',
             '20', '21']


types = ['TRAIN', 'TEST']

if not os.path.exists(os.path.dirname("reuters-fixed/")):
  os.makedirs(os.path.dirname("reuters-fixed/"))

file_out = open("reuters-fixed/reuters.csv", "w")

for file_num in file_nums:
  print file_num+" / 21"
  file_in_path = "reuters/reut2-0" + file_num + ".sgm"
  file_in = open(file_in_path)
  data = file_in.read().decode('utf8', errors='ignore')
  file_in.close()

  html_data = etree.HTML(data)
  result = etree.tostring(html_data, pretty_print=True, method="html")

  parsed = html.fromstring(result)
  for body in parsed:
    for reuters in body:
      split = reuters.attrib['lewissplit'].strip()
      date = reuters.find('date').text_content()

      text = reuters.find('text')

      title = text.find('title')
      if title is None:
        title = ""
      else:
        title = title.text_content().strip('\r\n')

      dateline = text.find('dateline')
      if dateline is None:
        dateline = ""
      else:
        dateline = dateline.text_content().replace('\n', ' ').replace('\r', ' ')

      text_body = text.text_content()
      text_body = text_body.replace('\n', ' ').replace('\r', ' ')
      text_body = text_body.replace(';', ' ')
      text_body = text_body.replace(title, ' ');
      text_body = text_body.replace(dateline, ' ');

      line = "" + split + ";" + date + ";" + title + ";"  + text_body + "\n"

      if split in types:
        if not "B l a h" in text_body:
          file_out.write(line)
  
file_out.close()
print "done.\n"


