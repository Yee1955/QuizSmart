using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;
using Backend_DB.Models;

namespace Backend_DB.Persistence
{
    public partial class Context : DbContext
    {
        public virtual DbSet<Employee> Employees { get; set; } = null!;
        public virtual DbSet<EmployeeSession> EmployeeSessions { get; set; } = null!;
        public virtual DbSet<Employer> Employers { get; set; } = null!;
        public virtual DbSet<Session> Sessions { get; set; } = null!;

        public Context()
        {
        }

        public Context(DbContextOptions<Context> options) : base(options)
        {
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder
                .UseNpgsql("Host=localhost;Database=sit305;Username=postgres;Password=")
                .LogTo(Console.Write)
                // .LogTo(Console.WriteLine, new[] {DbLoggerCategory.Database.Name })
                .EnableSensitiveDataLogging();
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Employee>(entity =>
            {
                entity.ToTable("employee");

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .UseIdentityAlwaysColumn();

                entity.Property(e => e.Email)
                    .HasMaxLength(255)
                    .HasColumnName("email");

                entity.Property(e => e.FullName)
                    .HasMaxLength(255)
                    .HasColumnName("full_name");

                entity.Property(e => e.Password)
                    .HasMaxLength(255)
                    .HasColumnName("password");
            });

            modelBuilder.Entity<EmployeeSession>(entity =>
            {
                entity.ToTable("employee_session");

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .UseIdentityAlwaysColumn();

                entity.Property(e => e.AnswerString)
                    .HasMaxLength(1000)
                    .HasColumnName("answer_string");

                entity.Property(e => e.EmployeeId).HasColumnName("employee_id");

                entity.Property(e => e.Progress).HasColumnName("progress");

                entity.Property(e => e.ScoreAlignment).HasColumnName("score_alignment");

                entity.Property(e => e.ScoreCommunication).HasColumnName("score_communication");

                entity.Property(e => e.ScoreInnovation).HasColumnName("score_innovation");

                entity.Property(e => e.ScoreProblemSolving).HasColumnName("score_problem_solving");

                entity.Property(e => e.ScoreTeamFit).HasColumnName("score_team_fit");

                entity.Property(e => e.SessionId).HasColumnName("session_id");

                entity.Property(e => e.Status)
                    .HasMaxLength(10)
                    .HasColumnName("status");

                entity.Property(e => e.Summary)
                    .HasMaxLength(1000)
                    .HasColumnName("summary");

                entity.HasOne(d => d.Employee)
                    .WithMany(p => p.EmployeeSessions)
                    .HasForeignKey(d => d.EmployeeId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_employee");

                entity.HasOne(d => d.Session)
                    .WithMany(p => p.EmployeeSessions)
                    .HasForeignKey(d => d.SessionId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_session");
            });

            modelBuilder.Entity<Employer>(entity =>
            {
                entity.ToTable("employer");

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .UseIdentityAlwaysColumn();

                entity.Property(e => e.CompanyName)
                    .HasMaxLength(255)
                    .HasColumnName("company_name");

                entity.Property(e => e.Email)
                    .HasMaxLength(255)
                    .HasColumnName("email");

                entity.Property(e => e.Password)
                    .HasMaxLength(255)
                    .HasColumnName("password");
            });

            modelBuilder.Entity<Session>(entity =>
            {
                entity.ToTable("session");

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .UseIdentityAlwaysColumn();

                entity.Property(e => e.CompanyCulture)
                    .HasMaxLength(1000)
                    .HasColumnName("company_culture");

                entity.Property(e => e.EmployerId).HasColumnName("employer_id");

                entity.Property(e => e.JobPosition)
                    .HasMaxLength(100)
                    .HasColumnName("job_position");

                entity.Property(e => e.JobRequirement)
                    .HasMaxLength(1000)
                    .HasColumnName("job_requirement");

                entity.Property(e => e.JobResponsibilities)
                    .HasMaxLength(1000)
                    .HasColumnName("job_responsibilities");

                entity.Property(e => e.QuestionString)
                    .HasMaxLength(100000)
                    .HasColumnName("question_string");

                entity.Property(e => e.SessionCode)
                    .HasMaxLength(10)
                    .HasColumnName("session_code");

                entity.Property(e => e.Status)
                    .HasMaxLength(100)
                    .HasColumnName("status");

                entity.HasOne(d => d.Employer)
                    .WithMany(p => p.Sessions)
                    .HasForeignKey(d => d.EmployerId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_employer");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
