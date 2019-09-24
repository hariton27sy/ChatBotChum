from urllib.request import urlopen
import sys
import re
import json

ingredients_file = r"..\src\com\ChatBot\DataBases\Dishes\Ingredients.JSON"
ingredients_file_reverse = r'..\src\com\ChatBot\DataBases\Dishes\IngredientToID.JSON'


def generate_food_link(counter):
    for i in range(counter, 140000):
        yield "https://www.russianfood.com/recipes/recipe.php?rid=" + str(i)


def get_page(counter=0):
    for link in generate_food_link(counter):
        print(link)
        with urlopen(link) as l:
            data = l.read().decode('windows-1251')
            if data.find(r'<table class="ingr"') != -1:
                yield data


def get_ingredients_table(data):
    with open("test.html", mode='w') as f:
        f.write(data)
        # print(data)
        # print(re.search(r'<table class="ingr" align="center">[^\\]*?</table>', data))  # [^\r]*?</table>
        return re.search(r'<table class="ingr" align="center">[^\\]*?</table>', data).group()


def get_ingredient_from_table(table):
    return re.findall(r'<span class="">(.*?)&nbsp;</span>', table)


def update_ingredients_data(counter=0):
    try:
        with open(ingredients_file, 'r') as f:
            ingredients_data = json.load(f)
        with open(ingredients_file_reverse, 'r') as f:
            ingredients_to_id = json.load(f)

        try:
            max_id = max(map(int, ingredients_data.keys())) + 1
        except:
            max_id = 1
        for page in get_page():
            ingredients = get_ingredient_from_table(get_ingredients_table(page))
            for ingr in ingredients:
                if ingr not in ingredients_to_id:
                    ingredients_data[str(max)] = {
                        "name": ingr,
                        "type": "vegetable",
                        "dishes_ids": {}
                    }
                    ingredients_to_id[ingr] = max_id
                    max_id += 1

            if counter % 10 == 0:
                with open(ingredients_file, 'w') as f:
                    json.dump(ingredients_data, f)
                with open(ingredients_file_reverse, 'w') as f:
                    json.dump(ingredients_to_id, f)

            counter += 1
    except KeyboardInterrupt:
        return counter


def main():
    print(update_ingredients_data())


if __name__ == "__main__":
    main()
