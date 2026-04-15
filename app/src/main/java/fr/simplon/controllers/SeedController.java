package fr.simplon.controllers;

import java.io.IOException;

import fr.simplon.models.Attachment;
import fr.simplon.models.Comment;
import fr.simplon.models.Post;
import fr.simplon.models.User;
import fr.simplon.utils.DataStore;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/seed")
public class SeedController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Avoid seeding twice
        if (!DataStore.getUsers().isEmpty()) {
            resp.setContentType("text/plain;charset=UTF-8");
            resp.getWriter().write("Données déjà initialisées. Rien à faire.");
            return;
        }

        // ── Users ──
        User michel   = new User("Michel",    "michel@example.com",   "password");
        User pascal   = new User("Pascal",    "pascal@example.com",   "password");
        User maxime   = new User("Maxime",    "maxime@example.com",   "password");
        User paul     = new User("Paul",      "paul@example.com",     "password");
        User basil    = new User("Basil",     "basil@example.com",    "password");

        DataStore.addUser(michel); // id 1
        DataStore.addUser(pascal);   // id 2
        DataStore.addUser(maxime);   // id 3
        DataStore.addUser(paul);     // id 4
        DataStore.addUser(basil);    // id 5

        // ── Posts (non-drafts only, skipping groups) ──

        // Post 1 – Michel
        Post post1 = new Post(michel,
            "From the makers of `ruff` comes [`uv`](https://astral.sh/blog/uv)\n\n"
            + "> TL;DR: `uv` is an extremely fast Python package installer and resolver, written in Rust, "
            + "and designed as a drop-in replacement for `pip` and `pip-tools` workflows.\n\n"
            + "It is also capable of replacing `virtualenv`.\n\n"
            + "With this announcement, the [`rye`](https://github.com/mitsuhiko/rye) project and package "
            + "management solution created by u/mitsuhiko (creator of Flask, minijinja, and so much more) "
            + "in Rust, will be maintained by the [astral](https://github.com/astral-sh/) team.\n\n"
            + "This \"merger\" and announcement is all working toward the goal of a `Cargo`-type project "
            + "and package management experience, but for Python.\n\n"
            + "For those of you who have big problems with the state of Python's package and project "
            + "management, this is a great set of announcements...\n\n"
            + "For everyone else, there is https://xkcd.com/927/.\n\n"
            + "- [Twitter Announcement](https://twitter.com/charliermarsh/status/1758216803275149389)\n"
            + "- [PyPI](https://pypi.org/project/uv/)\n"
            + "- [GitHub](https://github.com/astral-sh/uv)\n\n"
            + "Install it today:\n\n"
            + "```\npip install uv\n# or\npipx install uv\n# or\n"
            + "curl -LsSf https://astral.sh/uv/install.sh | sh\n```");
        DataStore.addPost(post1);

        // Post 2 – Maxime
        Post post2 = new Post(maxime,
            "Exciting to see, after many years, serious work in enabling multithreading that takes "
            + "advantage of multiple CPUs in a more effective way in Python. One step at a time: "
            + "https://github.com/python/cpython/pull/116338");
        DataStore.addPost(post2);

        // Post 3 – Basil
        Post post3 = new Post(basil,
            "I have been using python to code for almost 2 years and wanted to know what all IDEs "
            + "people use ? So I can make a wise choice. TIA");
        DataStore.addPost(post3);

        // Post 4 – Pascal
        Post post4 = new Post(pascal,
            "Found a cool resource which explains the CLI tools hidden in the Python Standard Library.\n\n"
            + "Link : [https://til.simonwillison.net/python/stdlib-cli-tools]"
            + "(https://til.simonwillison.net/python/stdlib-cli-tools)");
        DataStore.addPost(post4);

        // Post 5 – Michel (draft)
        Post post5 = new Post(michel,
            "```py\nclass Movable:\n    def __init__(self, x, y, dx, dy, worldwidth, worldheight):\n"
            + "        #automatically sets the given arguments.\n\n"
            + "        nonmembers = []\n\n"
            + "        for key, value in list(locals().items())[1:]:\n"
            + "            if not key in nonmembers:\n"
            + "                setattr(self, key, value)\n\n"
            + "        return\n```\n"
            + "I always hate how redundant and bothersome it is to type \"self.member = member\" 10+ times, "
            + "and this code does work the way I want it to.");
        post5.setDraft(true);
        DataStore.addPost(post5);

        // Post 6 – Michel
        Post post6 = new Post(michel,
            "Exciting to see, after many years, serious work in enabling multithreading that takes "
            + "advantage of multiple CPUs in a more effective way in Python. One step at a time: "
            + "https://github.com/python/cpython/pull/116338");
        DataStore.addPost(post6);

        // ── Comments ──

        // Comment 7 on post 6 – Jean Marc
        Post comment7 = new Comment(michel, "Maybe use dataclasses?", post6);
        DataStore.addPost(comment7);

        // Comment 8 on comment 7 – Basil
        Post comment8 = new Comment(basil,
            "Oh wow those are useful, good to know about them. But this implementation does allow me "
            + "to have other parameters in the __init__ method that aren't directly members themselves "
            + "and rather contribute to the calculation of other members. Do data classes allow this "
            + "in some way?",
            comment7);
        DataStore.addPost(comment8);

        // Comment 9 on post 5 – Pascal
        Post comment9 = new Comment(pascal, "Thanks", post5);
        DataStore.addPost(comment9);

        // ── Posts with Attachments ──

        // Post 10 – Michel – with image attachment (Radion CLI)
        Post post10 = new Post(michel, "Radion, an internet radio CLI client, written in Bash.");
        Attachment imgAttachment = new Attachment(
            "https://preview.redd.it/radion-an-internet-radio-cli-client-written-in-bash-v0-bf28wvpwjync1.png?auto=webp&s=5ea01b080358157f0d8b19962b541b742a89d9b0");
        post10.addAttachment(imgAttachment);
        DataStore.addPost(post10);
        DataStore.addAttachment(imgAttachment);

        // Post 11 – Michel – sharing post 10
        Post post11 = new Post(michel, "Check out this cool Bash project!");
        Attachment postAttachment = new Attachment(post10);
        post11.addAttachment(postAttachment);
        DataStore.addPost(post11);
        DataStore.addAttachment(postAttachment);

        // ── Likes ──

        // Post 1 liked by Michel, Pascal, Maxime
        DataStore.addLike(michel.getId(), post1.getId());
        DataStore.addLike(pascal.getId(),   post1.getId());
        DataStore.addLike(maxime.getId(),   post1.getId());

        // Post 2 liked by Michel
        DataStore.addLike(michel.getId(), post2.getId());

        // Post 3 liked by Jean Marc, Pascal
        DataStore.addLike(michel.getId(), post3.getId());
        DataStore.addLike(pascal.getId(),   post3.getId());

        // ── Follows (from group memberships) ──
        // Michel follows Pascal, Pascal follows Michel
        DataStore.addFollow(michel.getId(), pascal.getId());
        DataStore.addFollow(pascal.getId(),   michel.getId());

        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().write("Données initialisées avec succès !\n\n"
            + "Utilisateurs : Michel, Pascal, Maxime, Paul, Basil\n"
            + "Mot de passe : password (pour tous)\n"
            + "Posts : 8 (dont 1 brouillon)\n"
            + "Commentaires : 3\n"
            + "Likes : 6\n"
            + "Attachments : 2 (1 image, 1 post partagé)");
    }
}
