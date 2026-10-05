import { Component } from '@angular/core';

interface SkillCategory {
  index: string;
  title: string;
  description: string;
  skills: string[];
  wide?: boolean;
}

@Component({
  selector: 'app-skills',
  imports: [],
  templateUrl: './skills.component.html',
  styleUrl: './skills.component.css',
})
export class SkillsComponent {
  readonly categories: SkillCategory[] = [
    {
      index: '01',
      title: 'Backend',
      description: 'Resilient services and clean APIs.',
      skills: [
        'Java',
        'Spring',
        'Spring Boot',
        'REST API',
        'DSA',
        'Microservices',
        'System Design',
      ],
    },
    {
      index: '02',
      title: 'Frontend & CMS',
      description: 'Modern UIs and content platforms.',
      skills: ['Angular', 'AEM', 'TypeScript'],
    },
    {
      index: '03',
      title: 'Testing',
      description: 'Quality gates you can trust.',
      skills: ['JUnit', 'Mockito', 'Apache JMeter API Testing'],
    },
    {
      index: '04',
      title: 'DevOps',
      description: 'Containerised, orchestrated delivery.',
      skills: ['Docker', 'Kubernetes', 'CI/CD', 'Jenkins', 'Linux', 'YAML'],
    },
    {
      index: '05',
      title: 'Monitoring',
      description: 'Keeping an eye on system health and performance.',
      skills: ['Dynatrace', 'Splunk', 'Nagios', 'Pingdom'],
    },
    {
      index: '06',
      title: 'LLM & AI',
      description: 'Leveraging large language models and AI technologies.',
      skills: [
        'GitHub Copilot',
        'Cursor/ Claude code',
        'Copilot 365 Premium',
        'Copilot Custom Agent Creation',
        'RAG',
      ],
    },
  ];
}
