
package in.startupready.catalog;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StepCatalog {

    public record AssessmentField(
            String key,
            String label,
            String type,
            boolean required,
            String helpText) {
    }

    public record AssessmentStep(
            int number,
            String title,
            String category,
            String description,
            List<AssessmentField> fields) {
    }

    private final List<AssessmentStep> steps = List.of(
        step(1, "Startup Overview", "Foundation",
            "Introduce your startup, its purpose and its direction.",
            field("startup_name", "Startup name", "text", true, "Enter your startup's name."),
            field("industry", "Industry", "text", true, "Which industry do you operate in?"),
            field("current_stage", "Current stage", "select", true, "Idea, prototype, MVP, or growth."),
            field("headquarters", "Headquarters", "text", false, "City and country."),
            field("describe_your_startup_in_one_sentence", "Startup description", "textarea", true, "Describe what your startup does."),
            field("vision", "Vision", "textarea", false, "Your long-term aspiration."),
            field("mission", "Mission", "textarea", false, "Your purpose and approach.")
        ),
        step(2, "Problem & Solution Validation", "Validation",
            "Explain the problem and demonstrate that your solution addresses it.",
            field("problem_statement", "Problem statement", "textarea", true, "What specific problem do customers face?"),
            field("target_customer", "Target customer", "textarea", true, "Who experiences this problem?"),
            field("existing_alternatives", "Existing alternatives", "textarea", true, "How do customers solve it today?"),
            field("solution_description", "Solution description", "textarea", true, "How does your product solve the problem?"),
            field("customer_interviews", "Customer interviews", "number", false, "Number of interviews completed."),
            field("validation_evidence", "Validation evidence", "textarea", false, "Summarize customer feedback or experiments.")
        ),
        step(3, "Market Opportunity", "Market",
            "Describe your market size, customers and competition.",
            field("target_market", "Target market", "textarea", true, "Describe your target market."),
            field("tam", "Total addressable market (TAM)", "number", false, "Estimated total market value."),
            field("sam", "Serviceable available market (SAM)", "number", false, "Market you can realistically serve."),
            field("som", "Serviceable obtainable market (SOM)", "number", false, "Near-term obtainable market."),
            field("competitors", "Competitors", "textarea", true, "List competitors and alternatives."),
            field("competitive_advantage", "Competitive advantage", "textarea", true, "Why will customers choose you?")
        ),
        step(4, "Product Readiness", "Product",
            "Assess product maturity, usability and customer feedback.",
            field("product_stage", "Product stage", "select", true, "Idea, prototype, MVP, beta or launched."),
            field("core_features", "Core features", "textarea", true, "Describe the key product capabilities."),
            field("product_roadmap", "Product roadmap", "textarea", false, "Planned improvements and milestones."),
            field("user_feedback", "User feedback", "textarea", false, "What have users told you?"),
            field("product_metrics", "Product metrics", "textarea", false, "Usage, retention or engagement metrics.")
        ),
        step(5, "Technology Assessment", "Technology",
            "Explain your architecture, security and technical readiness.",
            field("technology_stack", "Technology stack", "textarea", true, "Languages, frameworks and infrastructure."),
            field("architecture", "System architecture", "textarea", true, "Describe major components and integrations."),
            field("data_security", "Data security", "textarea", true, "Access control, privacy and security practices."),
            field("scalability", "Scalability", "textarea", false, "How will the system handle growth?"),
            field("technical_risks", "Technical risks", "textarea", false, "Known technical constraints or dependencies.")
        ),
        step(6, "Business Model", "Business",
            "Explain how your startup creates, delivers and captures value.",
            field("business_model", "Business model", "select", true, "Subscription, transaction, licensing or other."),
            field("pricing_strategy", "Pricing strategy", "textarea", true, "How do you price your offering?"),
            field("revenue_streams", "Revenue streams", "textarea", true, "Current and planned revenue sources."),
            field("unit_economics", "Unit economics", "textarea", false, "Acquisition cost, margins and lifetime value."),
            field("key_partners", "Key partners", "textarea", false, "Important partners and suppliers.")
        ),
        step(7, "Team Assessment", "Team",
            "Describe founder capabilities, roles and hiring needs.",
            field("founder_background", "Founder background", "textarea", true, "Relevant skills and experience."),
            field("team_members", "Team members and roles", "textarea", true, "Who is on the team and what do they do?"),
            field("skills_gaps", "Skills gaps", "textarea", false, "Capabilities the team still needs."),
            field("hiring_plan", "Hiring plan", "textarea", false, "Planned hires and timing."),
            field("advisors", "Advisors and mentors", "textarea", false, "Relevant advisors or mentors.")
        ),
        step(8, "Financial Readiness", "Finance",
            "Summarize financial performance, projections and funding needs.",
            field("current_revenue", "Current revenue", "number", false, "Revenue to date or per month."),
            field("monthly_burn", "Monthly burn rate", "number", false, "Monthly operating cash outflow."),
            field("cash_runway", "Cash runway (months)", "number", false, "Estimated months of remaining cash."),
            field("financial_projections", "Financial projections", "textarea", true, "Forecast assumptions and milestones."),
            field("funding_required", "Funding required", "number", true, "Amount of funding sought."),
            field("use_of_funds", "Use of funds", "textarea", true, "How will the funding be used?")
        ),
        step(9, "Legal & Compliance", "Compliance",
            "Identify legal structure, intellectual property and regulatory obligations.",
            field("legal_structure", "Legal structure", "text", true, "Company or entity structure."),
            field("registration_status", "Registration status", "text", true, "Registration and incorporation status."),
            field("intellectual_property", "Intellectual property", "textarea", false, "Patents, trademarks, copyrights or ownership."),
            field("regulatory_requirements", "Regulatory requirements", "textarea", true, "Applicable regulations and licences."),
            field("privacy_compliance", "Privacy and data compliance", "textarea", false, "Relevant privacy practices and obligations."),
            field("legal_risks", "Legal risks", "textarea", false, "Known legal or compliance concerns.")
        ),
        step(10, "Sales & Go-to-Market", "Sales",
            "Explain customer acquisition, sales channels and market entry.",
            field("customer_acquisition", "Customer acquisition strategy", "textarea", true, "How will you acquire customers?"),
            field("sales_channels", "Sales channels", "textarea", true, "Direct, online, partners or other channels."),
            field("marketing_strategy", "Marketing strategy", "textarea", true, "How will customers discover your startup?"),
            field("sales_cycle", "Sales cycle", "textarea", false, "Describe your typical sales process."),
            field("go_to_market_plan", "Go-to-market plan", "textarea", true, "Key launch activities and milestones.")
        ),
        step(11, "Traction & Growth", "Growth",
            "Provide evidence of progress and explain your growth strategy.",
            field("customers", "Number of customers", "number", false, "Current paying or active customers."),
            field("growth_rate", "Growth rate", "text", false, "Growth over a defined period."),
            field("retention", "Customer retention", "textarea", false, "Repeat use, churn or retention data."),
            field("partnerships", "Partnerships", "textarea", false, "Active partnerships and their impact."),
            field("growth_strategy", "Growth strategy", "textarea", true, "How will you grow from here?")
        ),
        step(12, "ESG & Impact", "Impact",
            "Describe environmental, social and governance practices and impact.",
            field("environmental_impact", "Environmental impact", "textarea", true, "Environmental effects and initiatives."),
            field("social_impact", "Social impact", "textarea", true, "Effects on people and communities."),
            field("governance_practices", "Governance practices", "textarea", true, "Accountability, policies and oversight."),
            field("impact_metrics", "Impact metrics", "textarea", false, "How do you measure your impact?"),
            field("esg_goals", "ESG goals", "textarea", false, "Future ESG targets and timelines.")
        ),
        step(13, "Investment Readiness", "Investment",
            "Explain your fundraising plan and investor readiness.",
            field("fundraising_stage", "Fundraising stage", "select", true, "Bootstrapped, pre-seed, seed or later."),
            field("valuation_expectation", "Valuation expectation", "text", false, "Expected valuation and assumptions."),
            field("investor_targets", "Target investors", "textarea", true, "Investor types or target funds."),
            field("investment_thesis", "Investment thesis", "textarea", true, "Why is this a compelling investment?"),
            field("due_diligence_readiness", "Due diligence readiness", "textarea", false, "Availability of key documents.")
        ),
        step(14, "Risk Assessment", "Risk",
            "Identify major risks, their likelihood and mitigation plans.",
            field("market_risks", "Market risks", "textarea", true, "Demand, competition and market risks."),
            field("financial_risks", "Financial risks", "textarea", true, "Cash flow, funding and cost risks."),
            field("operational_risks", "Operational risks", "textarea", true, "Delivery, people and process risks."),
            field("technology_risks", "Technology risks", "textarea", true, "Security, reliability and technical risks."),
            field("risk_mitigation", "Risk mitigation plan", "textarea", true, "Actions to reduce the key risks.")
        ),
        step(15, "Final AI Evaluation", "Final Evaluation",
            "Review readiness scores, strengths, gaps and recommended actions.",
            field("founder_comments", "Additional founder comments", "textarea", false, "Anything else evaluators should know.")
        )
    );

    public List<AssessmentStep> all() {
        return steps;
    }

    public AssessmentStep get(int number) {
        return steps.stream()
                .filter(step -> step.number() == number)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid assessment step: " + number));
    }

    private static AssessmentStep step(
            int number,
            String title,
            String category,
            String description,
            AssessmentField... fields) {
        return new AssessmentStep(number, title, category, description, List.of(fields));
    }

    private static AssessmentField field(
            String key,
            String label,
            String type,
            boolean required,
            String helpText) {
        return new AssessmentField(key, label, type, required, helpText);
    }
}
