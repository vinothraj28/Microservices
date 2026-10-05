import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface Project {
  number: string;

  title: string;
  subtitle: string;

  context: 'Personal' | 'Professional';

  contextLabel: string;
  category: string;

  visualLabel: string;

  overview: string;

  contributions: string[];

  engineering: string[];

  technologies: string[];

  link?: string;
}

@Component({
  selector: 'app-projects',
  imports: [CommonModule],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.css',
})
export class ProjectsComponent {
  selectedProjectIndex = 0;

  projects: Project[] = [
    /* =====================================================
       01 — PERSONAL PROJECT
    ===================================================== */

    {
      number: '01',

      title: 'Secure Backend Platform',

      subtitle: 'IAM + High-Concurrency Booking System',

      context: 'Personal',

      contextLabel: 'Personal Engineering Project',

      category: 'Backend Engineering',

      visualLabel: 'IAM · Gateway · Services · PostgreSQL',

      overview:
        'A backend platform designed to explore secure authentication, authorization, service communication and concurrency-safe booking workflows.',

      contributions: [
        'Implemented OAuth2 Authorization Code and Client Credentials flows',
        'Built JWT-based authentication and token lifecycle management',
        'Implemented Role-Based Access Control and client registration',
        'Designed concurrency-safe seat allocation to prevent double booking',
        'Introduced Spring Cloud API Gateway and gRPC communication',
      ],

      engineering: [
        'Authentication & authorization',
        'Distributed service communication',
        'Concurrency control',
        'Transactional data integrity',
        'Relational data modelling',
      ],

      technologies: [
        'JAVA',
        'SPRING BOOT',
        'SPRING CLOUD',
        'POSTGRESQL',
        'gRPC',
        'OAUTH2',
      ],

      // Change this once you create the project page.
      // link: '/projects/secure-backend-platform'
    },

    /* =====================================================
       02 — EXACT
    ===================================================== */

    {
      number: '02',

      title: 'Enterprise Platform & Integrations',

      subtitle: 'Platform Engineering · Data · Salesforce',

      context: 'Professional',

      contextLabel: 'Professional Experience',

      category: 'Platform Engineering',

      visualLabel: 'PLATFORM · ETL · SALESFORCE · CLOUD',

      overview:
        'Enterprise platform work spanning reusable product capabilities, automated data pipelines, cloud services and Salesforce integrations across Exact products.',

      contributions: [
        'Architected and delivered a centralized Notification Center adopted across multiple Exact products',
        'Developed 5+ automated ETL pipelines for cross-system data synchronization',
        'Led integration of three Exact web applications with Salesforce',
        'Designed a cloud-based survey platform with reporting and analytics integration',
        'Collaborated on AI-driven chatbot capabilities using RAG architecture',
      ],

      engineering: [
        'Microservice-oriented systems',
        'Enterprise integrations',
        'Data synchronization',
        'ETL automation',
        'Cloud-based platforms',
      ],

      technologies: [
        'API',
        'JAVA',
        'ANGULAR',
        'TYPESCRIPT',
        'SQL',
        'ETL',
        'SALESFORCE',
        'AZURE',
      ],

      // Add a case study URL when you have one.
      // link: '/projects/enterprise-platform'
    },

    /* =====================================================
       03 — WIPRO
    ===================================================== */

    {
      number: '03',

      title: 'Legacy Java Modernization',

      subtitle: 'Application Migration · Production Delivery',

      context: 'Professional',

      contextLabel: 'Professional Experience',

      category: 'Java Engineering',

      visualLabel: 'JAVA · SPRING · LINUX · DEPLOYMENT',

      overview:
        'Modernization and production delivery work focused on migrating legacy Java applications, improving backend workflows and supporting reliable application releases.',

      contributions: [
        'Worked on migration of legacy web applications to newer technology stacks',
        'Developed automated backend processing workflows',
        'Supported testing, defect resolution and production stabilization',
        'Managed deployment configuration and infrastructure setup',
        'Supported application delivery through go-live',
      ],

      engineering: [
        'Legacy modernization',
        'Backend processing',
        'Production support',
        'Deployment engineering',
        'Linux-based environments',
      ],

      technologies: [
        'JAVA',
        'SPRING',
        'LINUX',
        'MAVEN',
        'TOMCAT',
        'BATCH JOBS',
      ],

      // link: '/projects/java-modernization'
    },
    {
      number: '04',
      title: 'Microservices Platform - Java + Angular Development',

      subtitle: 'Production Delivery & Support',

      context: 'Professional',

      contextLabel: 'Professional Experience',

      category: 'Java Engineering',

      visualLabel:
        'Java · Spring · Angular · Microservices · IAM · REST API · Docker · Jenkins',

      overview:
        'Building and maintaining a microservices platform using Java and Angular. Integrating various microservices and ensuring smooth production delivery.Ensuring high availability and performance of the platform. Set up monitoring and alerting mechanisms to proactively address issues.',

      contributions: [
        'Designed and implemented reliable Java application hosted in AEM servers',
        'Integrated various microservices and ensured smooth production delivery',
        'Ensured high availability and performance of the platform',
        'Set up monitoring in Dynatrace, Pingdom, Splunk, Nagios with alerting mechanisms to proactively address issues',
        'Collaborated with cross-functional & third party teams to ensure seamless integration and delivery',
        'Participated in code reviews and knowledge sharing sessions to improve team capabilities',
        'Mentored junior developers and provided guidance on best practices',
      ],

      engineering: [
        'Microservices architecture',
        'IAM integration',
        'Deployment engineering',
        'Monitoring and alerting',
      ],

      technologies: [
        'JAVA',
        'SPRING',
        'ANGULAR',
        'MICROSERVICES',
        'IAM',
        'REST API',
        'DOCKER',
        'JENKINS',
      ],

      // link: '/projects/new-project'
    },
  ];

  /**
   * Currently selected project.
   *
   * The getter keeps the HTML clean:
   *
   * {{ selectedProject.title }}
   */
  get selectedProject(): Project {
    return this.projects[this.selectedProjectIndex];
  }

  /**
   * Change the active project.
   */
  selectProject(index: number): void {
    if (index < 0 || index >= this.projects.length) {
      return;
    }

    this.selectedProjectIndex = index;
  }
}
