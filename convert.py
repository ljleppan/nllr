from lxml import etree
from lxml import html

file_nums = ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', 
             '10', '11', '12', '13', '14', '15', '16', '17', '18', '19',
             '20', '21', '22']

for file_num in file_nums:

  file_in_path = "reuters/reut2-0" + file_num + ".sgm"
  file_in = open(file_in_path)
  data = file_in.read()
  file_in.close()

  html_data = etree.HTML(data)
  result = etree.tostring(html_data, pretty_print=True, method="html")

  file_out_path = "reuters-fixed/" + file_num + ".csv"
  file_out = open(file_out_path, "w")

  parsed = html.fromstring(result)
  for body in parsed:
    for reuters in body:
      type = reuters.attrib['lewissplit']

      date = reuters.find('date').text_content()

      text = reuters.find('text')

      title = text.find('title')
      if title is None:
        title = ""
      else:
        title = title.text_content()

      dateline = text.find('dateline')
      if dateline is None:
        dateline = ""
      else:
        dateline = dateline.text_content()

      text_body = text.text_content()
      text_body = text_body.replace('\n', ' ').replace('\r', ' ')
      text_body = text_body.replace(';', ' ')
      text_body = text_body.replace(dateline, ' ')

      line = "" + type + ";" + title + ";" + dateline + ";" + text_body + "\n"

      file_out.write(line)

  file_out.close()  


