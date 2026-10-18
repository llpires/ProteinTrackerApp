#!/bin/bash
# Script para inicializar repo Git e fazer push para GitHub
# Execute este script na pasta do projeto

set -e

echo "🚀 Inicializando repositório Git..."

# Verifica se já é um repo git
if [ -d ".git" ]; then
    echo "⚠️  Já é um repositório Git. Pulando init..."
else
    git init
    echo "✅ Git init feito"
fi

# Adiciona todos os arquivos
git add .

# Commit inicial
git commit -m "feat: ProteinTracker App - inicial

- App Android nativo (Kotlin + Jetpack Compose)
- 50 receitas do Chef Proteína para 190g proteína/dia
- 5 telas: Home, Logger, Receitas, Progresso, Configurações
- Room DB + Hilt DI + Coroutines Flow
- GitHub Actions workflow para build APK automático"

echo "✅ Commit inicial criado"

# Instruções para o usuário
echo ""
echo "=========================================="
echo "📋 PRÓXIMOS PASSOS (execute manualmente):"
echo "=========================================="
echo ""
echo "1. Crie o repositório no GitHub:"
echo "   https://github.com/new"
echo "   Nome: ProteinTrackerApp"
echo "   Público ou Privado (sua escolha)"
echo "   NÃO inicialize com README, .gitignore ou license"
echo ""
echo "2. Adicione o remote e faça push:"
echo "   git remote add origin https://github.com/SEU_USUARIO/ProteinTrackerApp.git"
echo "   git branch -M main"
echo "   git push -u origin main"
echo ""
echo "3. O GitHub Actions vai buildar automaticamente!"
echo "   Vá em: https://github.com/SEU_USUARIO/ProteinTrackerApp/actions"
echo ""
echo "4. Para baixar o APK:"
echo "   - Clique no workflow 'Build Android APK' concluído"
echo "   - Baixe o artifact 'app-debug-apk'"
echo "   - Ou crie uma tag: git tag v1.0.0 && git push origin v1.0.0"
echo "   - Isso criará um Release com APKs anexados"
echo ""
echo "=========================================="