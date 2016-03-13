import urllib2

response = urllib2.urlopen("http://ultraimg.com/api/1/upload/?key=3374fa58c672fcaad8dab979f7687397&source=http://images.nationalgeographic.com/wpf/media-live/photos/000/936/cache/bear-road-denali_93621_990x742.jpg&format=txt").read()

print(response)
