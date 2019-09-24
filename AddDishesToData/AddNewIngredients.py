from urllib.request import urlopen
import sys
import re


def generate_food_link():
    for i in range(1, 140000):
        yield "https://www.russianfood.com/recipes/recipe.php?rid=" + str(i)


def get_ingredients_table(link):
    print(link)
    with urlopen(link) as l:
        data = l.read().decode('utf-8', errors='ignore')
        # with open("test.html", mode='wb') as f:
        #    f.write(data)
        return re.match(r'<table class="ingr" align="center">.*([\s]*.*)*</table>', data)


def main():
    print(get_ingredients_table(iter(generate_food_link()).__next__()))


if __name__ == "__main__":
    main()