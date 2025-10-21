// Jenkinsfile in family-tree-api-java repository
pipeline {
    agent any

    environment {
        REGISTRY = 'localhost:5000'
        IMAGE_NAME = 'family-tree-api-java'
        DB_CONTAINER = 'postgres-family-tree-java-test'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'echo "Checked out code successfully"'
            }
        }

        stage('Setup Test Database') {
            steps {
                script {
                    // Start test database container
                    sh """
                        docker stop ${DB_CONTAINER} || true
                        docker rm ${DB_CONTAINER} || true
                        docker run -d \\
                        --name ${DB_CONTAINER} \\
                        --platform=linux/arm64 \\
                        -e POSTGRES_PASSWORD=postgres \\
                        -e POSTGRES_DB=familytree_test \\
                        -p 5441:5432 \\
                        postgres:15-alpine
                    """

                    // Wait for database to be ready
                    sh 'sleep 10'
                    sh 'docker exec ${DB_CONTAINER} pg_isready -U postgres || echo "Database not ready yet"'
                }
            }
        }

        stage('Build with Maven') {
            steps {
                script {
                    sh '''
                        echo "Building with Maven in Docker..."
                        docker run --rm -v "$(pwd)":/app -w /app maven:3.9-eclipse-temurin-17 sh -c "
                            apt-get update && apt-get install -y nodejs npm &&
                            mvn clean package -DskipTests
                        "
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def image = docker.build("${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}")
                    docker.withRegistry("http://${REGISTRY}") {
                        image.push()
                        image.push('latest')
                    }
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'build/api-collection.json', fingerprint: true, allowEmptyArchive: true
            }
        }

        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                script {
                    // Create network for container communication
                    sh """
                        docker network create family-tree-java-network || echo "Network already exists"
                    """

                    // Stop existing containers
                    sh """
                        docker stop ${IMAGE_NAME} || true
                        docker rm ${IMAGE_NAME} || true
                        docker stop family-tree-java-db || true
                        docker rm family-tree-java-db || true
                    """

                    // Start database with correct name and settings
                    sh """
                        docker run -d \\
                        --name family-tree-java-db \\
                        --platform=linux/arm64 \\
                        --network family-tree-java-network \\
                        -e POSTGRES_PASSWORD=postgres \\
                        -e POSTGRES_DB=familytree \\
                        -p 5442:5432 \\
                        postgres:15-alpine
                    """

                    sleep 10

                    // Initialize database schema and data
                    sh """
                        cat /Users/chris/dev/family-tree-db/sql/schema.sql | docker exec -i family-tree-java-db psql -U postgres -d familytree
                        cat /Users/chris/dev/family-tree-db/sql/sample-data.sql | docker exec -i family-tree-java-db psql -U postgres -d familytree
                    """

                    // Run API container
                    sh """
                        docker run -d \\
                        --name ${IMAGE_NAME} \\
                        --restart unless-stopped \\
                        --network family-tree-java-network \\
                        -p 3200:3000 \\
                        -e DB_HOST=family-tree-java-db \\
                        -e DB_PORT=5432 \\
                        -e DB_NAME=familytree \\
                        -e DB_USER=postgres \\
                        -e DB_PASSWORD=postgres \\
                        ${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}
                    """
                }
            }
        }

        stage('Health Check') {
            when {
                branch 'main'
            }
            steps {
                script {
                    // Wait for API to be healthy with retries
                    sh '''
                        echo "⏳ Waiting for API to be healthy..."
                        for i in $(seq 1 30); do
                            if curl -f -s http://localhost:3200/health > /dev/null 2>&1; then
                                echo "✅ API is healthy after $((i * 10)) seconds"
                                break
                            elif [ $i -eq 30 ]; then
                                echo "❌ API failed to become healthy after 300 seconds"
                                echo "📋 Container logs:"
                                docker logs family-tree-api-java --tail 100
                                exit 1
                            else
                                echo "⏳ Attempt $i/30: API not ready, waiting 10s..."
                                sleep 10
                            fi
                        done
                    '''
                }
            }
        }

        stage('API Tests') {
            when {
                branch 'main'
            }
            steps {
                script {
                    sh '''
                        echo "🧪 Running API test suite..."
                        # Basic API connectivity tests using curl
                        curl -f http://localhost:3200/health
                        echo "✅ Basic API tests passed"
                    '''
                }
            }
            post {
                success {
                    echo '✅ All API tests passed!'
                }
                failure {
                    echo '❌ API tests failed'
                }
            }
        }
    }

    post {
        always {
            script {
                // Cleanup test database
                sh """
                    docker stop ${DB_CONTAINER} || true
                    docker rm ${DB_CONTAINER} || true
                """
            }
        }
        success {
            echo 'API pipeline completed successfully!'
        }
        failure {
            echo 'API pipeline failed!'
        }
    }
}
