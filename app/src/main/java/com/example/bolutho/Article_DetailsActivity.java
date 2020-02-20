package com.example.bolutho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bolutho.Fragments.ModelClasses.Articles;
import com.example.bolutho.Fragments.ModelClasses.RecyclerView_Adapter.Article_Paragraph;
import com.example.bolutho.Fragments.ParaghraphActivity;

import java.util.ArrayList;
import java.util.List;

public class Article_DetailsActivity extends AppCompatActivity implements Article_Paragraph.ClickListener {
    private RecyclerView recyclerView;
    List<Articles> list = new ArrayList<>();
    public Article_Paragraph article_paragraph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article__details);
        setList();
        Intent intent = getIntent();
        recyclerView = findViewById(R.id.recyclerView);

        article_paragraph = new Article_Paragraph(list);
        article_paragraph.setClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(article_paragraph);

    }

    public void setList() {
        Articles articles = new Articles();
        articles.setArticle_Heading("What is Sexual Harassment?");
        articles.setArticle_Paragraph("Sexual harassment involves unwanted or unwelcome behavior, which can offend," +
                " humiliate and intimidate a person while creating a hostile working environment.\n" +
                "\n" +
                "Sexual harassment includes but is not limited to:\n" +
                "\n" +
                "Making unwelcome sexual advances\n" +
                "Verbal harassment or abuse, verbal or written communication " +
                "(it includes narration of sexual incidents, emailing or messaging or showing explicit" +
                " sexual content in print or electronic form (SMS, Email, Screensavers, Posters, CDs etc)\n" +
                "Request for sexual favors (invitations for sex, requests for going out on dates)\n" +
                "Physical conduct (like touching, kissing, patting, pinching, physical assault like rape etc)\n" +
                "Sexually demeaning attitude (leering or staring at a person’s body\n" +
                "Any of above mentioned acts is included in harassment , if it is unwelcome and " +
                "is causing interference in work performance or creating a hostile working environment" +
                " or the harasser attempts to punish the complainant for refusal to comply with his/her requests " +
                "and makes sexual favors a condition of employment.");
        list.add(articles);
        Articles articlesOne = new Articles();
        articlesOne.setArticle_Heading("What are the laws relating to sexual harassment in Pakistan?");
        articlesOne.setArticle_Paragraph("Pakistan has enacted a new law namely “The Protection " +
                "Against Harassment of Women at Workplace Act, 2010” since last year. This is the" +
                " first time that sexual harassment has been defined in Pakistan through a legislative" +
                " instrument. Before this enactment, there was no" +
                " clear definition of harassment, whether at public, private or workplaces." +
                " Section 509 of Pakistan Penal Code 1860, talked about “insulting the modesty” of a " +
                "woman but there was no clear definition of “modesty”. Moreover, there was no law to" +
                " prohibit harassment at workplace.\n" +
                "\n" +
                "Last year (2010), Government of Pakistan not only enacted a special law for preventing " +
                "sexual harassment at workplace but also amended section 509 of Pakistan Penal Code. " +
                "Now it clearly defines harassment and includes harassment at workplace as well." +
                " It has also raised the maximum punishment for perpetrator from one to three years." +
                " Now under section 509 of Pakistan Penal Code, insulting the modesty of women or " +
                "sexually harassing them, is a crime. The perpetrator of this crime may be punished " +
                "with imprisonment, which may extend to 3 years or fine up to PKR 500,000 (5 lakh) or " +
                "with both. However, this crime is still bailable and compoundable (parties can settle the case " +
                "between themselves even when matter is in the court, after permission of the court).");
        list.add(articlesOne);
        Articles articlesTwo = new Articles();
        articlesTwo.setArticle_Heading("an you please explain in detail the issue of sexual harassment i.e." +
                " , how does it occur at workplaces?");
        articlesTwo.setArticle_Paragraph("According to the 2010 law, sexual harassment manifests itself at a" +
                " workplace" +
                " in following three forms\n" +
                "\n" +
                "“Abuse of authority” or Quid Pro Quo harassment …..demand of sexual favors by a person " +
                "in authority; a supervisor, a person in higher management, employer, and making it a condition of" +
                " obtaining certain job benefits which may  include\n" +
                "Wage increase\n" +
                "Promotion (to a higher grade)\n" +
                "Training opportunity (within or outside the country)\n" +
                "Transfer (to another place, department etc)\n" +
                "Job itself\n" +
                "“Creation of Hostile Working Environment”\n" +
                "any unwelcome advances ,\n" +
                "request for sexual favor,\n" +
                "other verbal or physical conduct ,\n" +
                "Which interferes with individual’s work performance or creates a hostile and " +
                "intimidating work environment\n" +
                "\n" +
                "Retaliation….If the victim refuses to grant sexual favors, the perpetrator can " +
                "retaliate in following ways: \n" +
                "Limiting an employee’s options for training, future promotions\n" +
                "Distorting the evaluation (annual confidential reports)\n" +
                "Generating gossip against the employee\n" +
                "Limiting access to his/her rights (right to complain, right to work" +
                " with dignity, right to promotions, wage increases etc)");
        list.add(articlesTwo);
        Articles articlesThree = new Articles();
        articlesThree.setArticle_Heading("If I become victim of sexual harassment, what should I do" +
                " according to this law?");
        articlesThree.setArticle_Paragraph("It is recommended that you should follow these steps whenever you " +
                "encounter sexual harassment.\n" +
                "\n" +
                "First Step…..You need to make it clear to harasser that you don’t like his/her" +
                " advances (his advances are unwelcome/unwarranted),\n" +
                "Second Step…..Even if you don’t want to make a formal complaint, do inform some" +
                " trustworthy colleague " +
                "in your organization,\n" +
                "Third Step….If you want to lodge a complaint in an informal way, you or your " +
                "designated person can informally report this incident to your supervisor or inquiry committee,\n" +
                "Fourth Step…..You can also launch a formal complaint to your supervisor or " +
                "inquiry committee through your supervisor, CBA (union) nominee or worker representative " +
                "(in case of absence of union),\n" +
                "Fifth Step….For filing a formal complaint, you have three options:\n" +
                "Either report the incident to Inquiry Committee, constituted within your" +
                " organization (Section 4)\n" +
                "Report directly to Federal/Provincial Ombudsman, appointed under this act (Section 8)\n" +
                "Report directly to Police (under Section 509 of PPC)\n" +
                "It is better to initiate complaint inside your organization.\n" +
                "\n" +
                "Sixth Step…. If you are not satisfied with decision of inquiry committee " +
                "and competent authority (of your organization), you can appeal to Ombudsman or" +
                " a District Court (in case, Ombudsmen are not appointed)\n" +
                "Seventh Step….If you are still aggrieved by decision of Ombudsman/District" +
                " Court, you can make a representation to President or Governor for justice.\n" +
                "If must be reminded that appeal option is available to all parties i.e.," +
                " both accused and victim can appeal against decisions.");
        list.add(articlesThree);
        Articles articlesFour = new Articles();
        articlesFour.setArticle_Paragraph("Not necessarily. Section 2 of this act says that it can include " +
                "“any situation that is linked to official work or official activity outside office”. So," +
                " it can occur outside office when a colleague is harassing other worker outside office" +
                " or while commuting on employer-provided transport, in a social event like employer organized" +
                " dinners, lunches, training event or dealing with clients outside one’s office. And the" +
                " workplace for a marketing employee is not" +
                " a building; rather the whole marketing area is her workplace.");
        articlesFour.setArticle_Heading("Is any unwelcome act occurring at workplace only to be" +
                " considered as sexual harassment?");
        list.add(articlesFour);
        Articles articlesFive = new Articles();
        articlesFive.setArticle_Paragraph("According to law, no adverse action can be taken against" +
                " complainant or witnesses (of any side) for lodging complaint of harassment. This is also" +
                " required of inquiry committee to make sure that complainant are not pressurized by employer" +
                " or accused to withdraw your complaint. Moreover, some part of the fine that convict is" +
                " required to pay, will be given to complainant as compensation.");
        articlesFive.setArticle_Heading("Can any employer discriminate against witnesses or " +
                "complainant for lodging complaint?");
        list.add(articlesFive);

        Articles articlesSix = new Articles();
        articlesSix.setArticle_Paragraph("If you lodged complaint within your organization i.e. to inquiry" +
                " committee, following two types of punishments can be given to the guilty person. The Competent" +
                " Authority can impose one or more of the following penalties" +
                " on recommendations of inquiry committee.");
        articlesSix.setArticle_Heading("What types of punishments are provided under this law?");
        list.add(articlesSix);
        Articles articlesSeven = new Articles();
        articlesSeven.setArticle_Paragraph("You are required under this law to constitute an Inquiry Committee," +
                " which will enquire into" +
                " all complaints of harassment. The Committee should have 3 members; at least one of them " +
                "must be a woman, one person from senior management and the other a workers’ representative " +
                "or a CBA representative (where union exists). You can also take members from outside the" +
                " organization , like some respectable member of community.\n" +
                "\n" +
                "It is your responsibility to ensure compliance with this act and incorporate the Code " +
                "of Conduct for protection against harassment at workplaces as part of your management" +
                " policy. You are also supposed to display copies of this code in English and other " +
                "language understood by majority of employees (be it Urdu, Balochi, Pashto, Punjabi or Sindhi)" +
                " at some prominent place (like notice board) and educate your workers about it.\n" +
                "\n" +
                "Moreover, if a victim of sexual harassment is in trauma, you are required to arrange for" +
                " him/her psychosocial counseling or medical treatment, besides granting additional medical " +
                "leave, if required. You are also required not to discriminate against a complainant.\n" +
                "\n" +
                "If you are found not complying with law, you can be liable to fine, which may" +
                " extend to PKR 100,000  (one lakh) but shall not be less than 25 thousand rupees.");
        articlesSeven.setArticle_Heading("I am an employer. What do I need to do to comply with this law?");
        list.add(articlesSeven);

        Articles articlesEight = new Articles();
        articlesEight.setArticle_Paragraph("The Inquiry Committee can recommend to Ombudsman for appropriate" +
                " action against complainant, if it is found that allegations leveled " +
                "against you are false and with mala fide intentions.");
        articlesEight.setArticle_Heading("What if I am accused of harassment by an alleged victim" +
                " with mala fide intentions?");
        list.add(articlesEight);

    }

    @Override
    public void onItemClick(int position, View view) {
        Articles articles=list.get(position);
        String paragraph=list.get(position).getArticle_Paragraph();
        Intent intent=new Intent(Article_DetailsActivity.this, ParaghraphActivity.class);
        intent.putExtra("detail",paragraph);
        startActivity(intent);



    }
}
