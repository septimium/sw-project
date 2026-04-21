package com.example.recipeapp.controller;

import com.example.recipeapp.model.Recipe;
import com.example.recipeapp.model.User;
import com.example.recipeapp.service.XmlDataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RecipeController {

    @Autowired
    private XmlDataService xmlDataService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("recipes", xmlDataService.getRecipesInMemory());
        return "index";
    }

    @GetMapping("/recommend-skill")
    public String recommendSkill(Model model) {
        model.addAttribute("recipes", xmlDataService.getRecipesBySkill());
        return "index";
    }

    @GetMapping("/recommend-skill-cuisine")
    public String recommendSkillCuisine(Model model) {
        model.addAttribute("recipes", xmlDataService.getRecipesBySkillAndCuisine());
        return "index";
    }

    @GetMapping(value = "/xslt", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String xsltView() {
        return xmlDataService.getRecipesXslt();
    }

    @GetMapping("/recipe-details")
    public String recipeDetails(@RequestParam("title") String title, Model model) {
        Recipe recipe = xmlDataService.getRecipeByTitle(title);
        model.addAttribute("recipe", recipe);
        return "recipe-details";
    }

    @GetMapping("/recipes-by-cuisine")
    public String recipesByCuisine(@RequestParam("cuisine") String cuisine, Model model) {
        model.addAttribute("recipes", xmlDataService.getRecipesByCuisine(cuisine));
        model.addAttribute("selectedCuisine", cuisine);
        return "index";
    }

    @GetMapping("/add-recipe")
    public String showRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        return "add-recipe";
    }

    @PostMapping("/add-recipe")
    public String addRecipe(@Valid @ModelAttribute("recipe") Recipe recipe, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-recipe";
        }
        xmlDataService.addRecipe(recipe);
        return "redirect:/";
    }

    @GetMapping("/add-user")
    public String showUserForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }

    @PostMapping("/add-user")
    public String addUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-user";
        }
        xmlDataService.addUser(user);
        return "redirect:/";
    }
}
