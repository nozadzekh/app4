package com.example.myapplication.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

class RecipeRepository {
    private val initialRecipes = listOf(
        Recipe(
            "1", 
            "ხინკალი კლასიკური", 
            "ქართული სამზარეულოს სიამაყე. წვნიანი, არომატული და ნამდვილი მთიულური რეცეპტით მომზადებული ხინკალი.", 
            listOf("საქონლის და ღორის ფარში (შერეული) - 1კგ", "ხახვი - 2 ცალი", "ძირა, წიწაკა, მარილი - გემოვნებით", "ფქვილი - 1.5კგ"), 
            "1. მოზილეთ მაგარი ცომი...\n2. ფარშს დაუმატეთ სანელებლები და წყალი...", 
            "https://images.unsplash.com/photo-1599481238505-b8b0537a3f77?q=80&w=800", 
            "ქართული", 
            "60 წთ", 
            "რთული"
        ),
        Recipe(
            "2", 
            "პიცა მარგარიტა", 
            "ნამდვილი იტალიური პიცა თხელი ცომით, ცოცხალი რეჰანითა და უმაღლესი ხარისხის მოცარელათი.", 
            listOf("პიცის ცომი - 300გრ", "ტომატის სოუსი - 100გრ", "მოცარელა - 200გრ"), 
            "1. გააცხელეთ ღუმელი მაქსიმუმზე...\n2. გააბრტყელეთ ცომი და გამოაცხვეთ...", 
            "https://images.unsplash.com/photo-1604068549290-dea0e4a305ca?q=80&w=800", 
            "იტალიური", 
            "25 წთ", 
            "მარტივი"
        ),
        Recipe(
            "3", 
            "აჭარული ხაჭაპური", 
            "ოქროსფერი ნავი, სადაც გამდნარი ყველი, კარაქი და კვერცხი საოცარ გემოს ქმნის.", 
            listOf("ცომი - 400გრ", "ყველი - 500გრ", "კვერცხი - 1 ცალი"), 
            "1. ცომს მიეცით ნავის ფორმა...\n2. ჩადეთ ყველი და გამოაცხვეთ...", 
            "https://images.unsplash.com/photo-1605333396915-47ed6b68a00e?q=80&w=800", 
            "ქართული", 
            "40 წთ", 
            "საშუალო"
        ),
        Recipe(
            "4", 
            "ორაგულის სუში როლი", 
            "ახალი ორაგულით, კრემ-ყველითა და ავოკადოთი მომზადებული პრემიუმ ხარისხის როლები.", 
            listOf("ბრინჯი - 200გრ", "ორაგული - 100გრ", "ავოკადო - 1/2 ცალი"), 
            "1. მოხარშეთ ბრინჯი სპეციალურად...\n2. გადაახვიეთ ნორიში...", 
            "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?q=80&w=800", 
            "აზიური", 
            "45 წთ", 
            "რთული"
        ),
        Recipe(
            "5", 
            "შოკოლადის ბრაუნი", 
            "სველი, ინტენსიური შოკოლადის გემო და ხრაშუნა ზედაპირი.", 
            listOf("შოკოლადი - 200გრ", "კარაქი - 150გრ", "შაქარი - 200გრ"), 
            "1. გაადნეთ შოკოლადი...\n2. გამოაცხვეთ 180 გრადუსზე...", 
            "https://images.unsplash.com/photo-1461023058943-07fcbe16d735?q=80&w=800", 
            "დესერტი", 
            "35 წთ", 
            "მარტივი"
        )
    )

    // თუ Firebase-ის URL არასწორია, აქ მიუთითეთ თქვენი კონსოლიდან აღებული ლინკი
    private val database = try {
        FirebaseDatabase.getInstance("https://my-application-17da8-default-rtdb.firebaseio.com/").getReference("recipes")
    } catch (e: Exception) {
        null
    }

    fun getRecipes(): Flow<List<Recipe>> = callbackFlow {
        // დაუყოვნებლივ ვაგზავნით ლოკალურ მონაცემებს
        trySend(initialRecipes).isSuccess

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val recipes = snapshot.children.mapNotNull { it.getValue(Recipe::class.java) }
                    if (recipes.isNotEmpty()) {
                        Log.d("RecipeRepository", "Firebase data received: ${recipes.size}")
                        trySend(recipes).isSuccess
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("RecipeRepository", "Firebase error: ${error.message}")
            }
        }
        
        database?.addValueEventListener(listener)
        awaitClose { database?.removeEventListener(listener) }
    }.onStart { emit(initialRecipes) }

    fun initializeDataIfNeeded() {
        database?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    initialRecipes.forEach { recipe ->
                        database.child(recipe.id).setValue(recipe)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("RecipeRepository", "Sync failed: ${error.message}")
            }
        })
    }
}
