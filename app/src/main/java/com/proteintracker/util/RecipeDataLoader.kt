package com.proteintracker.util

import android.content.Context
import com.proteintracker.data.model.MealType
import com.proteintracker.data.model.Recipe
import com.proteintracker.data.model.RecipeCategory
import com.proteintracker.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object RecipeDataLoader {
    private val json = Json { ignoreUnknownKeys = true }
    
    fun loadRecipesIfNeeded(context: Context, repository: Repository) {
        CoroutineScope(Dispatchers.IO).launch {
            val count = repository.getRecipeCount()
            if (count == 0) {
                val recipes = parseRecipesFromJson(context)
                repository.insertRecipes(recipes)
            }
        }
    }
    
    private fun parseRecipesFromJson(context: Context): List<Recipe> {
        // For now, return hardcoded recipes since we can't easily read assets in this context
        // In a real app, you'd load from assets/recipes.json
        return getHardcodedRecipes()
    }
    
    private fun getHardcodedRecipes(): List<Recipe> {
        return listOf(
            // DIA 1
            Recipe(
                name = "Omelete Supremo 4 Queijos + Frango",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 38.0,
                caloriesPerServing = 420,
                carbsPerServing = 5.0,
                fatPerServing = 24.0,
                prepTimeMinutes = 10,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "4 ovos inteiros",
                    "80g peito frango desfiado",
                    "50g queijo minas light",
                    "30g cottage",
                    "1 col sopa chia",
                    "Sal, pimenta, orégano",
                    "1 col chá azeite"
                )),
                instructions = json.encodeToString(listOf(
                    "Bata ovos com sal, pimenta, orégano",
                    "Aqueça frigideira antiaderente com azeite",
                    "Despeje ovos, cozinhe 1 min",
                    "Adicione frango, queijos, chia no centro",
                    "Dobre ao meio, tampe 2 min",
                    "Sirva quente"
                )),
                tips = "Frango pode ser sobra do almoço anterior. Cottage deixa cremoso por dentro.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Smoothie Power Whey + Pasta Amendoim",
                category = RecipeCategory.SWEET,
                proteinPerServing = 30.0,
                caloriesPerServing = 380,
                carbsPerServing = 35.0,
                fatPerServing = 16.0,
                prepTimeMinutes = 2,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "1 scoop whey baunilha (30g)",
                    "200ml leite desnatado",
                    "1 banana congelada",
                    "1 col sopa pasta amendoim",
                    "5g creatina",
                    "Gelo a gosto"
                )),
                instructions = json.encodeToString(listOf(
                    "Bata tudo no liquidificador 1 min",
                    "Ajuste consistência com mais leite se necessário",
                    "Beba imediato"
                )),
                tips = "Congele bananas maduras em pedaços para smoothies mais cremosos sem gelo.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Arroz + Feijão + 180g Frango Grelhado + Salada",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 45.0,
                caloriesPerServing = 580,
                carbsPerServing = 65.0,
                fatPerServing = 12.0,
                prepTimeMinutes = 25,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "180g peito frango cru (≈130g cozido)",
                    "100g arroz cru (≈300g cozido)",
                    "80g feijão cru (≈200g cozido)",
                    "Salada livre (alface, tomate, pepino)",
                    "1 col sopa azeite",
                    "Temperos: alho, cebola, limão, sal"
                )),
                instructions = json.encodeToString(listOf(
                    "Tempere frango com alho, limão, sal",
                    "Grelhe frango 6-8 min cada lado",
                    "Cozinhe arroz e feijão separados",
                    "Monte prato: arroz, feijão, frango fatiado, salada",
                    "Regue com azeite"
                )),
                tips = "Cozinhe frango em batch (1kg) domingo. Congele porções de 180g.",
                isMealPrep = true,
                isQuick = false
            ),
            
            Recipe(
                name = "Crepioca Proteica Pré-Treino",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 25.0,
                caloriesPerServing = 280,
                carbsPerServing = 28.0,
                fatPerServing = 8.0,
                prepTimeMinutes = 5,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "2 ovos",
                    "2 col sopa goma tapioca",
                    "30g queijo cottage",
                    "50g peito peru light",
                    "Orégano, sal"
                )),
                instructions = json.encodeToString(listOf(
                    "Misture ovos + tapioca + sal",
                    "Despeje frigideira quente antiaderente",
                    "Recheie com cottage + peru",
                    "Dobre, doure 1 min cada lado"
                )),
                tips = "Tapioca = carboidrato rápido. Perfeita 1h antes do treino.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Shake Pós-Treino: Whey + Leite + Aveia + Banana",
                category = RecipeCategory.SWEET,
                proteinPerServing = 32.0,
                caloriesPerServing = 380,
                carbsPerServing = 45.0,
                fatPerServing = 5.0,
                prepTimeMinutes = 1,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "1 scoop whey chocolate (30g)",
                    "250ml leite desnatado",
                    "30g aveia em flocos finos",
                    "1/2 banana",
                    "5g creatina"
                )),
                instructions = json.encodeToString(listOf(
                    "Bata no liquidificador 30 seg",
                    "Beba em até 30 min pós-treino"
                )),
                tips = "Aveia fina dissolve melhor. Se preferir frio, congele a banana.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Pote Proteico Noturno: Iogurte Grego + Whey + Frutas",
                category = RecipeCategory.SWEET,
                proteinPerServing = 25.0,
                caloriesPerServing = 220,
                carbsPerServing = 18.0,
                fatPerServing = 2.0,
                prepTimeMinutes = 3,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "200g iogurte grego 0%",
                    "1/2 scoop whey baunilha (15g)",
                    "100g morangos",
                    "10g chia",
                    "10g granola zero açúcar"
                )),
                instructions = json.encodeToString(listOf(
                    "Misture iogurte + whey até homogêneo",
                    "Pique morangos, misture",
                    "Finalize com chia + granola",
                    "Leve geladeira 10 min ou coma na hora"
                )),
                tips = "Preparo noturno: faz a mistura base (iogurte+whey) e guarda. Manhã só adiciona toppings.",
                isMealPrep = true,
                isQuick = true
            ),
            
            // DIA 2
            Recipe(
                name = "Panquecas Proteicas de Aveia + Banana (4 un)",
                category = RecipeCategory.SWEET,
                proteinPerServing = 35.0,
                caloriesPerServing = 420,
                carbsPerServing = 48.0,
                fatPerServing = 10.0,
                prepTimeMinutes = 10,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "3 ovos",
                    "60g aveia em flocos finos",
                    "1 banana madura",
                    "1 scoop whey baunilha (30g)",
                    "1 col chá fermento",
                    "Canela, 1 col chá óleo coco"
                )),
                instructions = json.encodeToString(listOf(
                    "Bata tudo no liquidificador",
                    "Aqueça frigideira, unte levemente",
                    "Despeje conchas pequenas",
                    "Vire quando borbulhar (2 min)",
                    "Sirva com 1 col sopa mel ou pasta amendoim"
                )),
                tips = "Massa rende 4 panquecas médias. Congele extras: toster direto na torradeira.",
                isMealPrep = true,
                isQuick = false
            ),
            
            Recipe(
                name = "Wrap de Frango + Cottage + Salada",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 28.0,
                caloriesPerServing = 320,
                carbsPerServing = 32.0,
                fatPerServing = 8.0,
                prepTimeMinutes = 5,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "1 wrap integral (50g)",
                    "100g frango desfiado temperado",
                    "50g cottage",
                    "Alface, tomate, cenoura ralada",
                    "1 col chá mostarda + iogurte (molho)"
                )),
                instructions = json.encodeToString(listOf(
                    "Aqueça wrap 30 seg frigideira",
                    "Passe molho, coloque alface, frango, cottage, cenoura",
                    "Enrole firme, corte ao meio"
                )),
                tips = "Molho: 1 col sopa iogurte grego + 1 col chá mostarda + sal = 20kcal.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Carne Magra (Patinho) 200g + Batata Doce + Brócolis",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 48.0,
                caloriesPerServing = 480,
                carbsPerServing = 42.0,
                fatPerServing = 14.0,
                prepTimeMinutes = 20,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "200g patinho limpo cru (≈150g cozido)",
                    "200g batata doce cozida",
                    "150g brócolis no vapor",
                    "1 col sopa azeite",
                    "Alho, sal rosa, pimenta, limão"
                )),
                instructions = json.encodeToString(listOf(
                    "Tempere carne 30 min antes",
                    "Grelhe carne 4-5 min cada lado (ponto mal)",
                    "Cozinhe batata doce + brócolis no vapor",
                    "Fatie carne contra fibras, regue com azeite + limão"
                )),
                tips = "Patinho = 28g proteína/100g cru. Melhor custo-benefício carne vermelha magra.",
                isMealPrep = true,
                isQuick = false
            ),
            
            Recipe(
                name = "Iogurte Grego + Whey + Castanhas Pré-Treino",
                category = RecipeCategory.SWEET,
                proteinPerServing = 25.0,
                caloriesPerServing = 280,
                carbsPerServing = 22.0,
                fatPerServing = 12.0,
                prepTimeMinutes = 2,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "170g iogurte grego 0%",
                    "1/2 scoop whey (15g)",
                    "15g castanhas (caju/amêndoas)",
                    "1/2 banana"
                )),
                instructions = json.encodeToString(listOf(
                    "Misture iogurte + whey",
                    "Pique castanhas + banana, misture",
                    "Consuma 60-90 min antes treino"
                )),
                tips = "Castanhas = gordura lenta = energia sustentada no treino.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Ovos Mexidos com Whey (Técnica Francesa) + Toasts",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 30.0,
                caloriesPerServing = 380,
                carbsPerServing = 22.0,
                fatPerServing = 20.0,
                prepTimeMinutes = 8,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "4 ovos",
                    "1/2 scoop whey neutro (15g)",
                    "1 col sopa leite desnatado",
                    "10g manteiga",
                    "2 fatias pão integral light",
                    "Cebolinha, sal"
                )),
                instructions = json.encodeToString(listOf(
                    "Bata ovos + whey + leite + sal",
                    "Derreta manteiga fogo BAIXO",
                    "Despeje ovos, mexa constantemente com espátula",
                    "Retire do fogo antes de secar totalmente (cremoso)",
                    "Sirva sobre toasts, salpique cebolinha"
                )),
                tips = "Fogo baixo + mexer constante = ovos cremosos estilo restaurante. Whey neutro não altera sabor.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Cottage Bowl Doce: Iogurte + Frutas + Granola",
                category = RecipeCategory.SWEET,
                proteinPerServing = 24.0,
                caloriesPerServing = 260,
                carbsPerServing = 28.0,
                fatPerServing = 6.0,
                prepTimeMinutes = 3,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "150g cottage",
                    "100g iogurte grego 0%",
                    "100g frutas vermelhas",
                    "15g granola proteica",
                    "10g chia",
                    "Stevia opcional"
                )),
                instructions = json.encodeToString(listOf(
                    "Misture cottage + iogurte",
                    "Adicione frutas, chia, granola",
                    "Doce a gosto"
                )),
                tips = "Cottage + iogurte = textura mousse. 30g proteína só nisso.",
                isMealPrep = false,
                isQuick = true
            ),
            
            // DIA 3
            Recipe(
                name = "Shake Verde Proteico: Espinafre + Whey + Abacate",
                category = RecipeCategory.SWEET,
                proteinPerServing = 36.0,
                caloriesPerServing = 350,
                carbsPerServing = 15.0,
                fatPerServing = 18.0,
                prepTimeMinutes = 2,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "1 scoop whey baunilha (30g)",
                    "200ml leite desnatado",
                    "1 xícara espinafre cru",
                    "1/2 abacate pequeno",
                    "1/2 lima (suco)",
                    "Gelo"
                )),
                instructions = json.encodeToString(listOf(
                    "Bata líquidos + espinafre primeiro",
                    "Adicione resto, bata até liso",
                    "Beba imediato (oxidação)"
                )),
                tips = "Espinafre some no sabor. Abacate dá cremosidade + gorduras boas.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Barras Proteicas Caseiras (2 un) - Lanche",
                category = RecipeCategory.SWEET,
                proteinPerServing = 30.0,
                caloriesPerServing = 320,
                carbsPerServing = 28.0,
                fatPerServing = 14.0,
                prepTimeMinutes = 15,
                servings = 4, // 8 barras total
                ingredients = json.encodeToString(listOf(
                    "60g whey (2 scoops)",
                    "80g aveia",
                    "60g pasta amendoim",
                    "30g mel",
                    "2 col sopa leite",
                    "30g gotas chocolate 70%"
                )),
                instructions = json.encodeToString(listOf(
                    "Misture seco: whey + aveia",
                    "Aqueça pasta amendoim + mel 20 seg micro",
                    "Misture tudo, adicione leite se seco",
                    "Pressione forma 20x20cm, gele 1h",
                    "Corte 8 barras = 30g proteína cada 2 barras"
                )),
                tips = "Guardam 10 dias geladeira. Congele por 3 meses.",
                isMealPrep = true,
                isQuick = false
            ),
            
            Recipe(
                name = "Tilápia Grelhada 200g + Quinoa + Aspargos",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 42.0,
                caloriesPerServing = 420,
                carbsPerServing = 40.0,
                fatPerServing = 10.0,
                prepTimeMinutes = 20,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "200g tilápia (≈150g cozida)",
                    "80g quinoa crua (≈240g cozida)",
                    "150g aspargos",
                    "1 col sopa azeite",
                    "Limão, alho, alecrim, sal"
                )),
                instructions = json.encodeToString(listOf(
                    "Tempere peixe com limão, alho, alecrim",
                    "Grelhe 4 min cada lado",
                    "Cozinhe quinoa (1:2 água, 15 min)",
                    "Salteie aspargos azeite + alho 5 min",
                    "Monte prato"
                )),
                tips = "Tilápia = 26g proteína/100g. Barata, leve, cozinha rápido.",
                isMealPrep = true,
                isQuick = false
            ),
            
            Recipe(
                name = "Tofu Mexido 'Scramble' + Toast Pré-Treino",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 28.0,
                caloriesPerServing = 320,
                carbsPerServing = 28.0,
                fatPerServing = 14.0,
                prepTimeMinutes = 10,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "200g tofu firme",
                    "2 ovos",
                    "1 col sopa levedura nutricional",
                    "Cúrcuma, pimenta, sal, cebola",
                    "1 fatia pão integral",
                    "1 col chá azeite"
                )),
                instructions = json.encodeToString(listOf(
                    "Desmanche tofu com garfo",
                    "Refogue cebola azeite",
                    "Adicione tofu + temperos, cozinhe 5 min",
                    "Bata ovos, junte ao tofu, mexa até cozinhar",
                    "Sirva com toast"
                )),
                tips = "Levedura nutricional = sabor 'queijo' + B12. Tofu = 16g proteína/100g.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Pós-Treino: Whey Isolado + Dextrose + Creatina",
                category = RecipeCategory.SWEET,
                proteinPerServing = 32.0,
                caloriesPerServing = 220,
                carbsPerServing = 30.0,
                fatPerServing = 0.5,
                prepTimeMinutes = 1,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "1 scoop whey isolado (30g)",
                    "30g dextrose (ou maltodextrina)",
                    "5g creatina",
                    "300ml água gelada"
                )),
                instructions = json.encodeToString(listOf(
                    "Misture tudo no shaker",
                    "Bata 10 seg, beba imediato"
                )),
                tips = "Isolado + dextrose = absorção máxima pós-treino. Janela 30 min.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Mousse Proteica Noturna: Chocolate + Morango",
                category = RecipeCategory.SWEET,
                proteinPerServing = 22.0,
                caloriesPerServing = 180,
                carbsPerServing = 12.0,
                fatPerServing = 3.0,
                prepTimeMinutes = 3,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "150g iogurte grego 0%",
                    "1/2 scoop whey chocolate (15g)",
                    "100g morangos",
                    "5g cacau 100%",
                    "5g chia"
                )),
                instructions = json.encodeToString(listOf(
                    "Bata iogurte + whey + cacau no mixer",
                    "Pique morangos, misture",
                    "Finalize chia, gele 15 min"
                )),
                tips = "Textura mousse real. 22g proteína, <200kcal. Satisfaz vontade doce.",
                isMealPrep = false,
                isQuick = true
            ),
            
            // DIA 4
            Recipe(
                name = "Muffins Proteicos Salgados (3 un) - Meal Prep",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 38.0,
                caloriesPerServing = 360,
                carbsPerServing = 12.0,
                fatPerServing = 18.0,
                prepTimeMinutes = 25,
                servings = 2, // 6 muffins total
                ingredients = json.encodeToString(listOf(
                    "6 ovos",
                    "150g frango desfiado",
                    "100g cottage",
                    "50g queijo ralado",
                    "Espinafre picado",
                    "Sal, pimenta, orégano",
                    "Forma muffins untada"
                )),
                instructions = json.encodeToString(listOf(
                    "Bata ovos + cottage + temperos",
                    "Divida frango + espinafre + queijo nas formas",
                    "Despeje mistura ovos 3/4 forma",
                    "Asse 180°C 20-25 min",
                    "Rende 6 muffins = 38g proteína 3 un"
                )),
                tips = "Faz domingo, come seg-sex. Congela 1 mês. Micro 1 min = café pronto.",
                isMealPrep = true,
                isQuick = false
            ),
            
            Recipe(
                name = "Ovo Cozido (3) + Fruta + Castanhas - Lanche",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 25.0,
                caloriesPerServing = 280,
                carbsPerServing = 18.0,
                fatPerServing = 16.0,
                prepTimeMinutes = 12,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "3 ovos cozidos",
                    "1 maçã ou pera",
                    "15g amêndoas"
                )),
                instructions = json.encodeToString(listOf(
                    "Cozinhe ovos 10 min (gema cremosa) ou 12 min (firme)",
                    "Descasque, tempere sal",
                    "Acompanhe fruta + castanhas"
                )),
                tips = "Cozinhe 14 ovos domingo = lanches semana toda.",
                isMealPrep = true,
                isQuick = true
            ),
            
            Recipe(
                name = "Chili Proteico: Carne Moída + Feijão Preto + Legumes",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 46.0,
                caloriesPerServing = 450,
                carbsPerServing = 38.0,
                fatPerServing = 14.0,
                prepTimeMinutes = 30,
                servings = 3,
                ingredients = json.encodeToString(listOf(
                    "200g patinho moído magro",
                    "100g feijão preto cozido",
                    "1/2 cebola, 1/2 pimentão, 2 dentes alho",
                    "400g tomate pelado",
                    "Cominho, pimenta, louro, sal",
                    "50g queijo minas ralado (opcional)",
                    "1 col sopa azeite"
                )),
                instructions = json.encodeToString(listOf(
                    "Refogue cebola, alho, pimentão azeite",
                    "Adicione carne, doure bem",
                    "Junte tomate, feijão, temperos, água",
                    "Cozinhe 20 min fogo médio",
                    "Finalize queijo por cima"
                )),
                tips = "Rende 3 porções = 46g cada. Congela perfeitamente. Sabor melhora no dia seguinte.",
                isMealPrep = true,
                isQuick = false
            ),
            
            Recipe(
                name = "Crepioca Doce: Whey + Banana + Pasta Amendoim",
                category = RecipeCategory.SWEET,
                proteinPerServing = 28.0,
                caloriesPerServing = 320,
                carbsPerServing = 32.0,
                fatPerServing = 12.0,
                prepTimeMinutes = 5,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "2 ovos",
                    "2 col sopa tapioca",
                    "1/2 scoop whey baunilha (15g)",
                    "1/2 banana amassada",
                    "1 col chá pasta amendoim",
                    "Canela"
                )),
                instructions = json.encodeToString(listOf(
                    "Misture ovos + tapioca + whey + banana + canela",
                    "Frigideira antiaderente, despeje",
                    "Recheie com pasta amendoim",
                    "Dobre, sirva"
                )),
                tips = "Doce, proteico, carbo rápido. Ideal 1h pré-treino pesado.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Shake Pós-Treino: Whey + Leite + Maltodextrina",
                category = RecipeCategory.SWEET,
                proteinPerServing = 30.0,
                caloriesPerServing = 300,
                carbsPerServing = 38.0,
                fatPerServing = 3.0,
                prepTimeMinutes = 1,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "1 scoop whey (30g)",
                    "250ml leite desnatado",
                    "25g maltodextrina",
                    "5g creatina"
                )),
                instructions = json.encodeToString(listOf(
                    "Shaker: água/leite + maltodextrina + whey + creatina",
                    "Agite bem, beba"
                )),
                tips = "Maltodextrina repõe glicogênio mais barato que dextrose.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Pudim Chia Proteico Noturno (Preparo Noturno)",
                category = RecipeCategory.SWEET,
                proteinPerServing = 23.0,
                caloriesPerServing = 240,
                carbsPerServing = 28.0,
                fatPerServing = 7.0,
                prepTimeMinutes = 3,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "200ml leite desnatado",
                    "1/2 scoop whey baunilha (15g)",
                    "2 col sopa chia (24g)",
                    "100g manga picada",
                    "Stevia"
                )),
                instructions = json.encodeToString(listOf(
                    "Misture leite + whey + chia + stevia",
                    "Leve geladeira 4h+ (ou noite)",
                    "Manhã: adicione manga, misture"
                )),
                tips = "Chia hidrata = gel. 10g fibras. Prep noturno = zero trabalho manhã.",
                isMealPrep = true,
                isQuick = true
            ),
            
            // DIA 5
            Recipe(
                name = "Bolo de Caneca Proteico Micro-ondas (Single Serve)",
                category = RecipeCategory.SWEET,
                proteinPerServing = 34.0,
                caloriesPerServing = 320,
                carbsPerServing = 28.0,
                fatPerServing = 10.0,
                prepTimeMinutes = 2,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "1 ovo",
                    "1 scoop whey chocolate (30g)",
                    "2 col sopa aveia",
                    "1 col sopa cacau 100%",
                    "1/2 banana amassada",
                    "1 col chá fermento",
                    "1 col chá óleo coco",
                    "Canela"
                )),
                instructions = json.encodeToString(listOf(
                    "Misture tudo caneca grande",
                    "Micro 90 seg potência alta",
                    "Teste palito; +15 seg se necessário",
                    "Desenforme ou coma na caneca"
                )),
                tips = "Pronto em 2 min. Satisfaz vontade bolo instantaneamente. 34g proteína.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Cookies Proteicos Aveia + Whey (4 un) - Lanche",
                category = RecipeCategory.SWEET,
                proteinPerServing = 30.0,
                caloriesPerServing = 300,
                carbsPerServing = 32.0,
                fatPerServing = 12.0,
                prepTimeMinutes = 18,
                servings = 2, // 8 cookies total
                ingredients = json.encodeToString(listOf(
                    "60g whey baunilha (2 scoops)",
                    "80g aveia",
                    "1 banana madura",
                    "1 ovo",
                    "30g gotas chocolate 70%",
                    "1 col chá fermento",
                    "Canela, pitada sal"
                )),
                instructions = json.encodeToString(listOf(
                    "Misture aveia + whey + fermento + canela + sal",
                    "Amasse banana, bata com ovo, junte seco",
                    "Misture gotas chocolate",
                    "Forme 8 cookies, asse 180°C 12-15 min",
                    "4 cookies = 30g proteína"
                )),
                tips = "Textura macia por dentro, crocante fora. Guardam 5 dias pote vedado.",
                isMealPrep = true,
                isQuick = false
            ),
            
            Recipe(
                name = "Frango Xadrez Proteico + Arroz Integral",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 44.0,
                caloriesPerServing = 480,
                carbsPerServing = 48.0,
                fatPerServing = 14.0,
                prepTimeMinutes = 20,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "180g peito frango cubos",
                    "1/2 pimentão vermelho, 1/2 amarelo",
                    "1/2 cebola cubos",
                    "50g amendoim torrado sem sal",
                    "2 col sopa shoyu light",
                    "1 col chá gengibre ralado",
                    "1 dente alho",
                    "1 col sopa azeite",
                    "100g arroz integral cru"
                )),
                instructions = json.encodeToString(listOf(
                    "Tempere frango shoyu + gengibre 15 min",
                    "Refogue legumes azeite 3 min, retire",
                    "Doura frango mesma panela",
                    "Volte legumes + amendoim, aqueça",
                    "Sirva com arroz integral"
                )),
                tips = "Amendoim = crocância + gordura boa + proteína extra. Shoyu light = sódio controlado.",
                isMealPrep = true,
                isQuick = false
            ),
            
            Recipe(
                name = "Vitamina Pré-Treino: Whey + Aveia + Banana + Café",
                category = RecipeCategory.SWEET,
                proteinPerServing = 26.0,
                caloriesPerServing = 300,
                carbsPerServing = 42.0,
                fatPerServing = 5.0,
                prepTimeMinutes = 2,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "1/2 scoop whey (15g)",
                    "200ml leite desnatado",
                    "30g aveia",
                    "1 banana",
                    "50ml café forte frio",
                    "Gelo"
                )),
                instructions = json.encodeToString(listOf(
                    "Bata tudo liquidificador",
                    "Beba 60 min pré-treino"
                )),
                tips = "Café = pré-treino natural. Aveia = carbo sustentado. Whey = proteína.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Pós-Treino: Whey + Batata Doce + Clara Ovo",
                category = RecipeCategory.SWEET,
                proteinPerServing = 32.0,
                caloriesPerServing = 320,
                carbsPerServing = 38.0,
                fatPerServing = 3.0,
                prepTimeMinutes = 5,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "1 scoop whey (30g)",
                    "150g batata doce cozida amassada",
                    "3 claras cozidas picadas",
                    "Canela",
                    "Stevia"
                )),
                instructions = json.encodeToString(listOf(
                    "Misture batata + whey + canela + stevia",
                    "Incorpore claras delicadamente",
                    "Coma morno ou frio"
                )),
                tips = "Batata doce = carbo médio índice glicêmico. Claras = proteína pura extra.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Brownie Proteico Micro-ondas (Ceia Doce)",
                category = RecipeCategory.SWEET,
                proteinPerServing = 24.0,
                caloriesPerServing = 250,
                carbsPerServing = 18.0,
                fatPerServing = 12.0,
                prepTimeMinutes = 2,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "1 scoop whey chocolate (30g)",
                    "1 ovo",
                    "2 col sopa cacau 100%",
                    "1 col sopa pasta amendoim",
                    "1 col chá fermento",
                    "1 col sopa leite",
                    "30g gotas chocolate 70%"
                )),
                instructions = json.encodeToString(listOf(
                    "Misture tudo ramekin/ caneca grande",
                    "Micro 60-75 seg",
                    "Centro deve ficar molhadinho",
                    "Deixe esfriar 2 min"
                )),
                tips = "Único brownie com 24g proteína e baixo carbo. Vicia.",
                isMealPrep = false,
                isQuick = true
            ),
            
            // BONUS 10
            Recipe(
                name = "Hambúrguer Caseiro 180g + Batata Doce Frita Airfryer",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 42.0,
                caloriesPerServing = 480,
                carbsPerServing = 42.0,
                fatPerServing = 16.0,
                prepTimeMinutes = 20,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "180g patinho moído",
                    "Sal, pimenta, alho pó",
                    "1 fatia queijo minas",
                    "Pão integral light",
                    "200g batata doce palitos",
                    "Pimenta fumada, azeite"
                )),
                instructions = json.encodeToString(listOf(
                    "Modele hambúrguer, grelhe 4 min lado",
                    "Últimos 30 seg: coloque queijo",
                    "Batata: tempere, airfryer 200°C 15 min",
                    "Monte burger + salada"
                )),
                tips = "Moída caseira = controle gordura. Airfryer = batata crocante sem óleo.",
                isMealPrep = false,
                isQuick = false
            ),
            
            Recipe(
                name = "Lasanha Proteica Berinjela (Low Carb)",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 40.0,
                caloriesPerServing = 380,
                carbsPerServing = 18.0,
                fatPerServing = 20.0,
                prepTimeMinutes = 45,
                servings = 4,
                ingredients = json.encodeToString(listOf(
                    "2 berinjelas grandes fatiadas",
                    "300g patinho moído",
                    "300g molho tomate caseiro",
                    "200g cottage + 100g queijo minas",
                    "1 ovo",
                    "Orégano, manjericão",
                    "1 col sopa azeite"
                )),
                instructions = json.encodeToString(listOf(
                    "Grelhe fatias berinjela dos dois lados",
                    "Refogue carne + molho 10 min",
                    "Misture cottage + queijo + ovo + ervas",
                    "Monte camadas: molho, berinjela, creme, repita",
                    "Finalize queijo, asse 180°C 30 min"
                )),
                tips = "Berinjela no lugar da massa = -80% carbo. Rende 4 porções.",
                isMealPrep = true,
                isQuick = false
            ),
            
            Recipe(
                name = "Salmão Grelhado + Purê Couve-Flor + Espinafre",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 45.0,
                caloriesPerServing = 420,
                carbsPerServing = 15.0,
                fatPerServing = 22.0,
                prepTimeMinutes = 20,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "180g salmão",
                    "300g couve-flor",
                    "200g espinafre",
                    "2 col sopa requeijão light",
                    "1 col sopa azeite",
                    "Limão, dill, alho, sal"
                )),
                instructions = json.encodeToString(listOf(
                    "Tempere salmão limão + dill + sal",
                    "Grelhe pele baixo 5 min, vire 3 min",
                    "Cozinhe couve-flor, bata com requeijão + sal",
                    "Refogue espinafre alho + azeite 2 min"
                )),
                tips = "Salmão = ômega-3 + 25g proteína/100g. Purê couve-flor = 50kcal vs 200kcal batata.",
                isMealPrep = false,
                isQuick = false
            ),
            
            Recipe(
                name = "Shakshuka Proteica (Ovos no Molho Tomate + Carne)",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 36.0,
                caloriesPerServing = 380,
                carbsPerServing = 18.0,
                fatPerServing = 20.0,
                prepTimeMinutes = 20,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "4 ovos",
                    "150g patinho moído",
                    "400g tomate pelado",
                    "1 cebola, 1 pimentão, 3 alhos",
                    "Cominho, páprica, pimenta",
                    "1 col sopa azeite",
                    "Salsa picada"
                )),
                instructions = json.encodeToString(listOf(
                    "Refogue cebola, pimentão, alho azeite",
                    "Adicione carne, doure",
                    "Junte tomate + temperos, cozinhe 10 min",
                    "Faça covas, quebre ovos dentro",
                    "Tampe, cozinhe 5-7 min (gema mole)",
                    "Salpique salsa"
                )),
                tips = "Um prato só. Rústico, saboroso, 36g proteína. Pão integral pra mergulhar.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Panqueca Proteica Salgada Recheada (2 un)",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 38.0,
                caloriesPerServing = 420,
                carbsPerServing = 28.0,
                fatPerServing = 18.0,
                prepTimeMinutes = 20,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "Massa: 3 ovos, 40g aveia, 1 scoop whey neutro, 100ml leite, sal",
                    "Recheio: 150g frango desfiado + 100g cottage + 50g queijo + milho",
                    "Molho: 200g tomate pelado + manjericão + alho"
                )),
                instructions = json.encodeToString(listOf(
                    "Bata massa liquidificador, frigideira fina 2 panquecas",
                    "Misture recheio, recheie, enrole",
                    "Cubra com molho quente, queijo por cima",
                    "Gratine 5 min forno"
                )),
                tips = "Massa neutra = doce ou salgada. Whey neutro não altera sabor.",
                isMealPrep = true,
                isQuick = false
            ),
            
            Recipe(
                name = "Bowl Mexicano: Carne + Feijão + Arroz + Guacamole",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 43.0,
                caloriesPerServing = 520,
                carbsPerServing = 52.0,
                fatPerServing = 18.0,
                prepTimeMinutes = 25,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "180g carne moída temperada",
                    "80g feijão preto",
                    "80g arroz",
                    "1/2 abacate + limão + sal (guac)",
                    "Pico de gallo (tomate, cebola, coentro, limão)",
                    "50g queijo minas ralado",
                    "1 col sopa creme ricota light"
                )),
                instructions = json.encodeToString(listOf(
                    "Cozinhe arroz + feijão",
                    "Refogue carne temperos mexicanos",
                    "Amasse abacate + limão + sal",
                    "Monte bowl: arroz, feijão, carne, guac, pico, queijo, creme"
                )),
                tips = "Estilo Chipotle caseiro. Macro perfeito. Guacamole = gordura boa.",
                isMealPrep = true,
                isQuick = false
            ),
            
            Recipe(
                name = "Quiche Proteica Frango + Espinafre (Fatia Generosa)",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 35.0,
                caloriesPerServing = 320,
                carbsPerServing = 15.0,
                fatPerServing = 18.0,
                prepTimeMinutes = 40,
                servings = 6,
                ingredients = json.encodeToString(listOf(
                    "Massa: 100g farinha aveia, 1 ovo, 50g iogurte grego, 50g queijo ralado, sal",
                    "Recheio: 4 ovos, 200ml leite, 150g frango, 100g espinafre, 100g cottage, noz-moscada"
                )),
                instructions = json.encodeToString(listOf(
                    "Misture massa, forre forma fundo+laterais",
                    "Bata ovos+leite+temp, junte frango+espinafre+cottage",
                    "Despeje sobre massa, asse 180°C 35 min"
                )),
                tips = "Massa proteica = farinha aveia + iogurte + queijo. Rende 6 fatias.",
                isMealPrep = true,
                isQuick = false
            ),
            
            Recipe(
                name = "Sorvete Proteico 3 Ingredientes (Banana + Whey + Leite)",
                category = RecipeCategory.SWEET,
                proteinPerServing = 28.0,
                caloriesPerServing = 220,
                carbsPerServing = 28.0,
                fatPerServing = 2.0,
                prepTimeMinutes = 5,
                servings = 1,
                ingredients = json.encodeToString(listOf(
                    "3 bananas congeladas",
                    "1 scoop whey baunilha (30g)",
                    "100ml leite desnatado",
                    "Toppings: cacau nibs, pasta amendoim, frutas"
                )),
                instructions = json.encodeToString(listOf(
                    "Congele bananas picadas 4h+",
                    "Bata processadora: banana + whey + leite",
                    "Textura soft-serve imediata",
                    "Congele 30 min se quiser mais firme"
                )),
                tips = "Zero açúcar adicionado. 28g proteína. Viciante.",
                isMealPrep = false,
                isQuick = true
            ),
            
            Recipe(
                name = "Overnight Oats Proteico (Prep Noturno 5 Dias)",
                category = RecipeCategory.SWEET,
                proteinPerServing = 32.0,
                caloriesPerServing = 380,
                carbsPerServing = 48.0,
                fatPerServing = 8.0,
                prepTimeMinutes = 10,
                servings = 5,
                ingredients = json.encodeToString(listOf(
                    "250g aveia",
                    "500ml leite desnatado",
                    "2 scoops whey baunilha (60g)",
                    "20g chia",
                    "500g frutas vermelhas congeladas",
                    "Stevia, canela"
                )),
                instructions = json.encodeToString(listOf(
                    "Misture aveia + leite + whey + chia + stevia + canela",
                    "Divida 5 potes",
                    "Cubra com frutas congeladas",
                    "Geladeira noite",
                    "Manhã: pronto, misture, coma"
                )),
                tips = "Prep domingo = 5 cafés manhã prontos. Frutas descongelam na noite = calda natural.",
                isMealPrep = true,
                isQuick = true
            ),
            
            Recipe(
                name = "Sopa Proteica Carne + Legumes (Congela Bem)",
                category = RecipeCategory.SAVORY,
                proteinPerServing = 38.0,
                caloriesPerServing = 320,
                carbsPerServing = 25.0,
                fatPerServing = 10.0,
                prepTimeMinutes = 40,
                servings = 6,
                ingredients = json.encodeToString(listOf(
                    "300g músculo/acém picado",
                    "200g feijão branco cozido",
                    "Cenoura, batata, abobrinha, vagem, tomate",
                    "Cebola, alho, louro, salsa",
                    "1 col sopa azeite",
                    "2L água/caldo caseiro"
                )),
                instructions = json.encodeToString(listOf(
                    "Doura carne azeite + cebola + alho",
                    "Adicione água, louro, cozinhe pressão 25 min",
                    "Junte legumes + feijão, +10 min pressão",
                    "Finalize salsa, acerte sal"
                )),
                tips = "Músculo = colágeno + proteína barata. Rende 6 porções. Congela 3 meses.",
                isMealPrep = true,
                isQuick = false
            )
        )
    }
}