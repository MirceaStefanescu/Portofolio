# Terraform layout

- `modules/platform` defines reusable infrastructure building blocks.
- `environments/dev` wires modules together with environment-specific inputs.

This layout mirrors the production structure where modules are versioned and composed per environment.
